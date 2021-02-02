# user-profile


sudo apt-get purge mariadb-*

sudo apt-get install mariadb-server

sudo gedit /etc/mysql/my.cnf
[mysqld]
bind-address=0.0.0.0

// create user...;
// SELECT user, host, password FROM mysql.user;
// flush privileges;

sudo service mysql restart


