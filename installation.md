# Installation Document OC-Explorer

## VPS (virtual server) requirements

- CPU: 4 cores
- Memory: 16GB
- Disk: 128GB SSD

## Software Prerequisites

- Operating system: Ubuntu Server x64 16.04 LTS or upper

## Install dependencies

### Install MongoDB

Please refer to this comprehensive documentation page about how to install MongoDB on Ubuntu
https://docs.mongodb.com/v3.0/tutorial/install-mongodb-on-ubuntu/


## Install OpenJDK 8.0

`$ sudo apt install openjdk-8-jdk`

## Create user oce

`$ sudo useradd -m oce`

## Option 1 - download and compile source code

### Install Maven

`$ sudo apt install maven`

### Get the source code


```
$ su - oce
$ git clone https://github.com/devgateway/oc-explorer.git
```

### Compile the code

```
$ cd oc-explorer
$ mvn install
```

### Copy artifact and config to startup locatinon

```
$ cd ~
$ cp forms/target/forms-*-SNAPSHOT.jar oce.jar
$ cp forms/forms.conf oce.conf
```

### Edit configuration file

Replace website.url with your website's URL

### Make symlink to enable startup as service

```
$ sudo ln -s /home/oce/oce.jar /etc/init.d/oce
$ sudo update-rc.d oce defaults
```

