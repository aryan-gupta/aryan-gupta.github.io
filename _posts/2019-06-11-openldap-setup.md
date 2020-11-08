---
title: "OpenLDAP Setup"
tags:
  - LDAP
  - OpenLDAP
---

**Note:** I wrote this blog post before my actual website went up. I cannot account for the correctness and the use of proper english on tis post. Please read with a grain of salt. I will remove this warning when I have had a chance to read over it and make sure its complete.
{: .notice--warning}

# Setting up a LDAP Server
On my goal of creating my home lab, I decided to add somthing similar to Windows Active Directory services so I could have one account that could access multiple services without tyoeing in different passwords. However, considering that I use linux for most of my day-to-day computing, I wanted to setup an OpenLDAP server on my Arch inux server. 

## LDAP
LDAP stands for Lightweight Directory Access Protocol. It is nothing more than a database. But it's primary focus is to be quickly be accessable (read) with less emphasis on writing. The lightweight part of its name comes from the fact that it is a slimmed down version of the X.500 standard. Like X.500, LDAP is a directory service, it allows various information such as DHCP and DNS records to be stored in its database. 

## Server setup
Installing OpenLDAP on Arch Linux is pretty straight forward. 

```shell
$ pacman -S openldap
```

Once the package manager installs the package, you can start editing the config file. However, after much research, I found out that after version 2.4.23, openldap moved to storeing its configuration inside of its ldap database rather than in a flat configuration. I decided that it would be best if I did minimal configuration using the flat configuration file, port it to the new OLC config format then add any other configuration that I needed to later on. In order to modify the flat config file, you can open this file using any sudo-permissive text editor.

```shell
$ nano /etc/openldap/slapd.conf
```

Below is the base configuration I had.

```properties
#
# See slapd.conf(5) for details on configuration options.
# This file should NOT be world readable.
#
include         /etc/openldap/schema/core.schema
include         /etc/openldap/schema/cosine.schema
include         /etc/openldap/schema/inetorgperson.schema
include         /etc/openldap/schema/nis.schema
include         /etc/openldap/schema/misc.schema
include         /etc/openldap/schema/samba.schema


# Define global ACLs to disable default read access.

# Do not enable referrals until AFTER you have a working directory
# service AND an understanding of referrals.
#referral       ldap://root.openldap.org

pidfile         /run/openldap/slapd.pid
argsfile        /run/openldap/slapd.args

# Load dynamic backend modules:
# modulepath    /usr/lib/openldap
# moduleload    back_mdb.la
# moduleload    back_ldap.la

# Sample security restrictions
#       Require integrity protection (prevent hijacking)
#       Require 112-bit (3DES or better) encryption for updates
#       Require 63-bit encryption for simple bind
# security ssf=1 update_ssf=112 simple_bind=64

# Sample access control policy:
#       Root DSE: allow anyone to read it
#       Subschema (sub)entry DSE: allow anyone to read it
#       Other DSEs:
#               Allow self write access
#               Allow authenticated users read access
#               Allow anonymous users to authenticate
#       Directives needed to implement policy:
access to dn.base="" by * read
access to dn.base="cn=Subschema" by * read
access to *
        by self write
        by users read
        by anonymous auth

# if no access controls are present, the default policy
# allows anyone and everyone to read anything but restricts
# updates to rootdn.  (e.g., "access to * by * read")
#
# rootdn can always read and write EVERYTHING!

#######################################################################
# MDB database definitions
#######################################################################

database        mdb
maxsize         1073741824
suffix          "dc=example,dc=com"
rootdn          "cn=Manager,dc=example,dc=com"
rootpw          your-password-here
# The database directory MUST exist prior to running slapd AND
# should only be accessible by the slapd and slap tools.
# Mode 700 recommended.
directory       /var/lib/openldap/openldap-data
# Indices to maintain
index   objectClass     eq

index   uid             pres,eq
index   mail            pres,sub,eq
index   cn              pres,sub,eq
index   sn              pres,sub,eq
index   dc              eq

#######################################################################
# config database definitions
#######################################################################
# before the first database definition
database config
rootdn "cn=Manager,cn=config"
rootpw your-password-here

```

The main changes to the file (from the default Arch Linux config) are noted below. The `include` derivatives tells openldap various types of data that can be stored in the database and its structure. We imported some of the basic schemas, but more can be added later. The `samba.schema` is not included by default and must be copied from the examples like so: `cp /usr/share/doc/samba/examples/LDAP/samba.schema /etc/openldap/schema`.

```properties
include         /etc/openldap/schema/cosine.schema
include         /etc/openldap/schema/inetorgperson.schema
include         /etc/openldap/schema/nis.schema
include         /etc/openldap/schema/misc.schema
include         /etc/openldap/schema/samba.schema
```

The next modification simply allows the basic access to the LDAP server database. More information can be found from the comments.

```properties

access to dn.base="" by * read
access to dn.base="cn=Subschema" by * read
access to *
        by self write
        by users read
        by anonymous auth

```

Here you can change your suffix to your domain suffix. I cam currently using `example.com`, but change it whatever you want to call your domain. For `rootpw` you can either set the password as plain text as I did in the example (not recommended) or run `slappasswd` to get a hashed password like this: `{SSHA}H23W7RmEQKcx98KA3UrM9N8ekKz3DTNB`. More details about the salting and hasing can be found [here] (https://serverfault.com/a/675846). 

```properties
suffix          "dc=example,dc=com"
rootdn          "cn=Manager,dc=example,dc=com"
rootpw          your-password-here
```

Next add the indexing properties for the database, these should be commonly used entries. The LDAP server will 'cache' these entries for faster access. 

```properties
index   uid             pres,eq
index   mail            pres,sub,eq
index   cn              pres,sub,eq
index   sn              pres,sub,eq
index   dc              eq
```

The last edit I did was to allow access to change the configuration of the LDAP server. This is the new way to modify your server without any downtime or restart of the server, more details on this later. Similar to the main database, you must specify a `rootdn` or the 'root' account. The account must be under `cn=config` or you will not be able to login properly. 


```properties
database config
rootdn "cn=Manager,cn=config"
rootpw your-password-here
```

Once I had my base configuration created, I created my base database by copying an example database.

```shell
$ cp /var/lib/openldap/openldap-data/DB_CONFIG.example /var/lib/openldap/openldap-data/DB_CONFIG
```

Before starting up the server, we must port the flat config file to the new OLC. First remove all the old files from the config folder. This must be done because LDAP will refuse to overwrite files if they already exist (I found out this the hard way). Then port the config file. Lastly, give permissions to the `ldap` user to read and write to that directory. 

```shell
$ rm -rf /etc/openldap/slapd.d/*
$ slaptest -f /etc/openldap/slapd.conf -F /etc/openldap/slapd.d/
$ chown -R ldap:ldap /etc/openldap/slapd.d
```

These 3 commands MUST be run everytime you change the flat config file (`/etc/openldap/slapd.conf`). However, we can take advantage of the OLC feature to edit configs on the fly. 

Finally you can start the database. 

```shell
$ systemctl enable --now slapd.service
```

## Server Configuration
In order to test that the configuration is valid, run this command. The command should print out the base configuration of the config database. 

```shell
$ ldapsearch -W -x -D 'cn=Manager,cn=config' -b 'olcDatabase={0}config,cn=config'
Enter LDAP Password: 
# extended LDIF
#
# LDAPv3
# base <olcDatabase={0}config,cn=config> with scope subtree
# filter: (objectclass=*)
# requesting: ALL
#

# {0}config, config
dn: olcDatabase={0}config,cn=config
objectClass: olcDatabaseConfig
olcDatabase: {0}config
olcAccess: {0}to *  by * none
olcAddContentAcl: TRUE
olcLastMod: TRUE
olcMaxDerefDepth: 15
olcReadOnly: FALSE
olcRootDN: cn=Manager,cn=config
olcRootPW: your-root-password
olcSyncUseSubentry: FALSE
olcMonitoring: FALSE

# search result
search: 2
result: 0 Success

# numResponses: 2
# numEntries: 1
```

In order to add or change the configuration, I created a ldif (does not mean ldap diff file, but) file. I added this ldif file to allow any users to change their own name, password, etc

```ldif
# Filename: access_ctrl.ldif

dn: olcDatabase={-1}frontend,cn=config
changetype: modify
delete: olcAccess
olcAccess: {0}
olcAccess: {1}
olcAccess: {2}

dn: olcDatabase={-1}frontend,cn=config
changetype: modify
add: olcAccess
olcAccess: {0}to attrs=userPassword,givenName,sn,photo by self write by anonymous auth by * none
olcAccess: {1}to * by self read by * read

```

YOu can add this ldif file by runing this command.

```shell
$ ldapadd -D "cn=Manager,cn=config" -W -f access_ctrl.ldif
```

Now we want to populate the LDAP server with the base data. The ldif file is as shown below.

```ldif
# example.org
dn: dc=example,dc=org
dc: example
o: Example Organization
objectClass: dcObject
objectClass: organization

# Manager, example.org
dn: cn=Manager,dc=example,dc=org
cn: Manager
description: LDAP administrator
objectClass: organizationalRole
objectClass: top
roleOccupant: dc=example,dc=org

# People, example.org
dn: ou=People,dc=example,dc=org
ou: People
objectClass: top
objectClass: organizationalUnit

# Groups, example.org
dn: ou=Group,dc=example,dc=org
ou: Group
objectClass: top
objectClass: organizationalUnit
```

Add the changes using `ldapadd`.

```shell
$ ldapadd -D "cn=Manager,dc=example,dc=org" -W -f base.ldif
Enter LDAP Password: 
adding new entry "dc=example,dc=com"

adding new entry "cn=Manager,dc=example,dc=com"

adding new entry "ou=People,dc=example,dc=com"

adding new entry "ou=Group,dc=example,dc=com"

```

Now that we have a basic tree for the LDAP database, we can start adding in the users. You can visit this webite to get [migration tools](https://www.padl.com/OSS/MigrationTools.html) that may help migrating users. In order order to migrate your account, you can download the source and run the script like this. 

```shell
$ sudo perl -I . ./migrate_passwd.pl /etc/passwd users.ldif
```

Then exctract the user you want to from the ldif file and write it to another file. My final ldif file contained:

```ldif
dn: uid=john,ou=People,dc=example,dc=com
objectClass: top
objectClass: person
objectClass: organizationalPerson
objectClass: inetOrgPerson
objectClass: posixAccount
objectClass: shadowAccount
uid: john
cn: john
sn: Doe
givenName: John
title: Generic Person
telephoneNumber: +1 555 555 5555
postalAddress: Address Line 1$Address Line 2$Address Line 3
userPassword: your-password-here
shadowLastChange: 12345
shadowMin: 0
shadowMax: 99999
shadowWarning: 7
labeledURI: https://example.com/
loginShell: /bin/bash
uidNumber: 10000
gidNumber: 10000
homeDirectory: /home/john/
description: User account for John Doe

```

You can add the ldif file using ldapadd.

```shell
$ ldapadd -D "cn=Manager,dc=example,dc=com" -W -f user_john.ldif      
Enter LDAP Password: 
adding new entry "uid=john,ou=People,dc=example,dc=com"

```




```
»ldapadd -D "cn=Manager,dc=gempi,dc=re" -W
Enter LDAP Password: 
dn: cn=aryan,ou=Group,dc=gempi,dc=re
changetype: modify
replace: gidNumber
gidNumber: 7500

modifying entry "cn=aryan,ou=Group,dc=gempi,dc=re"


```































