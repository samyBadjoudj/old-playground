# json-prettifier


Add these lines in /etc/apt/sources.list

deb http://ppa.launchpad.net/samy-badjoudj/json-prettifier/ubuntu xenial main 

deb-src http://ppa.launchpad.net/samy-badjoudj/json-prettifier/ubuntu xenial main 


sudo apt-get update;

sudo apt-get insall json-prettifier;


or download it directly : 

[get the package] (https://launchpad.net/~samy-badjoudj/+archive/ubuntu/json-prettifier/+files/json-prettifier_1.5-0ubuntu1_amd64.deb)

usage :
Put json as stdin : 
 cat json_file.json | json-prettifier.c 
 or cat json_file.json | json-prettifier | less -R


screenshot:


<img src="https://s31.postimg.org/3jp5u44ff/json_pretti.png" />
