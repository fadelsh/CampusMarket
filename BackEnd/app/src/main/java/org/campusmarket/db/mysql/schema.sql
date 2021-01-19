CREATE TABLE IF NOT EXISTS users(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username  VARCHAR(30) NOT NULL,  
    password   VARCHAR(16) NOT NULL,
    firstname VARCHAR(30),
    lastname  VARCHAR(30),
    email      VARCHAR(45),
    university VARCHAR(30),
    admin     BOOLEAN,

    UNIQUE (username),
    UNIQUE (email)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS session(
    sess_id   VARCHAR(16) NOT NULL PRIMARY KEY,
    admin     BOOLEAN
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS user_sessions(
    user_id  INT UNSIGNED NOT NULL,
    sess_id  VARCHAR(16) NOT NULL PRIMARY KEY,

    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (sess_id) REFERENCES session(sess_id)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS shopping_carts(
    user_id  INT UNSIGNED NOT NULL,
    item_id  INT UNSIGNED NOT NULL PRIMARY KEY,

    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (item_id) REFERENCES items(refnum)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS items(
	refnum INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR (20),
	price DOUBLE(6,2),
	category VARCHAR (30),
	cond VARCHAR (30),
	seller VARCHAR (30) NOT NULL,
	filename VARCHAR(45),
	filetype VARCHAR (30),
	img LONGBLOB,
	postdate DATE NOT NULL,
    checkout INT UNSIGNED NOT NULL,
	FOREIGN KEY (seller) REFERENCES users(username) ON DELETE CASCADE ON UPDATE CASCADE
) engine=InnoDB;

