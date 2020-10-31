---
title: Simple Discovery Protocol
updated: 2019-05-29 08:28
tags: 
  - udp
  - discovery
  - protocol
---

# Simple Discovery Protocol
One of the things I was interested is making my internal network as modular as possible. I wanted to remove the necessity of hardcoding server names and other configurations into the script file or program. So I decided to create a simple discovery protocol based on HTTP to get these values.


## Protocol
An HTTP packet is divided into 3 parts: the status line, the header fields, and the optional body.

```
GET / HTTP/1.1
Host: www.example.com

<body>
	optional body
</body>
```

Similarly, I decided to format the protocol very similarly. The status line is `TGED` for The Gupta Empire Discovery protocol, and the method is `DISCOVERY`, followed by the protocol version. For the client side, a message like this will be broadcasted to the network. The `RQ` is the request for what type of server the client needs.

```
TGED DISCOVERY 1.0
RQ:SQL

```

The server sill parse the packet and reply with a similarly structured packet.

```
TGED REPLY 1.0
HOST: test
FQDN: test.example.com
IP: 192.168.0.34
PORT: 3306
USER: john

```


If the server is not able to service the request, and invalid packet is sent.

```
TGED INVALID 1.0
RP: INVALID

```

## The Code
One of the things that I wanted to learn more was UDP communication. By design, UDP is a connections protocol. Meaning that there is no connection that is made to the server, in other words, its shoot-and-forget. The beauty of this is that the client can broadcast its request to the entire subnet and not need to have any server information hardcoded into the script.

### Server Design
The server will actively listen to the port for client requests. The port the server listens to was arbitrarily chosen.

```python
port = 4745

with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as sock:
	sock.settimeout(2)
	sock.bind(('0.0.0.0', port))
	message, address = sock.recvfrom(4096)
```

The server will parse the packet and make sure that the status line contains the valid protocol. It will then get the service details the client requested and reply back to the client. The services were places in a json config file, this allowed dynamically updating the services without restarting the server.[^json_file_dyn_update]. By setting the time out to 2 seconds, I was able to set a kill switch that checks if the server should shutdown.

```python
port = 4745
kill = threading.Event()

with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as sock:
	sock.settimeout(2)
	sock.bind(('0.0.0.0', port))

	while not kill.is_set():
		try:
			message, address = sock.recvfrom(4096)
		except socket.timeout:
			continue
```

The kill variable can be set from any thread, I chose to have a signal handler thread that would signal the server to stop. One interesting thing I learned was python lambda functions. It turns out that unlike C++ function template, python functions are objects and its reference can be passed around and created. For the signal handler, I did not want to make a global kill variable, so I opted for a 'lambda' function to handle the signal. By using nonlocal, I was able to bring the scope of the kill variable into the function, similar to reference capturing lambdas in C++

```python
def main():
	kill = Event()

	udp_thread = Thread(target=udp_server_work, args=(port, kill))
	udp_thread.start()

	def sig_handler(sig, frame):
		nonlocal kill, udp_thread
		kill.set()]
		udp_thread.join()
		sys.exit(0)
	signal.signal(signal.SIGINT, sig_handler)
	signal.pause()

if __name__ == "__main__":
	main()

```


### Client Design
The client was designed as a library so other python scripts could use it as a module. The script would call a function with the service it needs and the module would take care of creating the UDP socket and parsing the result. The client constructs the packet (`data`) and sends the packet. The socket must have the broadcast option turned on as a check to make sure we aren't accidentally flooding the network with packets. For UDP, a time should be set in case the server is down and can't get a reply. The module also adds the server it received its response from in case it is needed.

```python
HEADER = "TGED DISCOVERY 1.0"
DIS_IP = '255.255.255.255'
PORT = 4745

def get_discovery_response(service, timeout=10):
	data = '\r\n'.join([HEADER, f"RQ:{service}", '', ''])

	with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as sock:
		sock.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST, 1)
		sock.settimeout(timeout)
		sock.sendto(data.encode(), (DIS_IP, PORT))

		data, addr = sock.recvfrom(1024)
		reply = parse(data.decode()) # parse the packet into a dict

	reply['SVR'] = addr[0]
	return reply
```

## Future and Use Case
The current discovery server is used in my wake-on-lan script, where it uses the protocol to get the SQL server that contains the DHCP reservations and mac address of the host to wake. I have seen Flask load python server files dynamically (it detects changes and reloads them); I would like to see if I can implement this for the json service file. Or move the service file into a SQL database.

[^json_file_dyn_update] Currently not implemented, need to figure out how to watch for file changes and update the config.