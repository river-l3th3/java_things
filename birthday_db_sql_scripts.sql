CREATE TABLE `BIRTHDAYS` (
  `BDAY_NAME` varchar(120) NOT NULL,
  `BIRTHDATE` date DEFAULT NULL,
  `LINKED_NUMBER` varchar(16) NOT NULL DEFAULT '0',
  PRIMARY KEY (`BDAY_NAME`,`LINKED_NUMBER`),
  KEY `ix_test` (`BIRTHDATE`),
  KEY `BIX_BIRTHDATE` (`BIRTHDATE`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `CARRIERS` (
  `CARRIER_NAME` varchar(24) NOT NULL DEFAULT '',
  `MSG_ADDRESS` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`CARRIER_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `NUMBERS` (
  `CONTACT_NUMBER` varchar(12) NOT NULL DEFAULT '',
  `CARRIER` varchar(64) NOT NULL DEFAULT 'AT&T',
  `CARRIER_ADDRESS` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`CONTACT_NUMBER`,`CARRIER`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `add_bday`(IN_NAME varchar(256), BDAY_DATE varchar(24), LINKED_NUMBER varchar(24), CARRIER varchar(24))
BEGIN
	insert into BIRTHDAYS(BDAY_NAME,BIRTHDATE,LINKED_NUMBER)
	values
	(IN_NAME,cast(BDAY_DATE as date),LINKED_NUMBER);

	insert ignore into NUMBERS(CONTACT_NUMBER,CARRIER)
	values
	(LINKED_NUMBER,CARRIER);

	update NUMBERS
	left join CARRIERS ON NUMBERS.CARRIER = CARRIERS.CARRIER_NAME
	set NUMBERS.CARRIER_ADDRESS = CARRIERS.MSG_ADDRESS
	where NUMBERS.CARRIER_ADDRESS is null;

END$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `find_birthdays`()
BEGIN

declare today date; 
set today = cast(now() as date);

select 
BIRTHDAYS.BDAY_NAME
,BIRTHDAYS.LINKED_NUMBER
,NUMBERS.CARRIER_ADDRESS
,case
	when date_format(BIRTHDAYS.BIRTHDATE,"%m-%d") = date_format(today,"%m-%d")
	= date_format(BIRTHDAYS.BIRTHDATE,"%m-%d") = date_format(today,"%m-%d")
	then 'today'
	when date_format(BIRTHDAYS.BIRTHDATE,"%m-%d") = 
	date_format(date_add(today, interval 14 day),"%m-%d")
	then 'two weeks'
	when date_format(BIRTHDAYS.BIRTHDATE,"%m-%d") =  
	date_format(date_add(today, interval 7 day),"%m-%d")
	then 'one week'
	when date_format(BIRTHDAYS.BIRTHDATE,"%m-%d") =
	date_format(date_add(today, interval 1 day),"%m-%d")
	then 'tomorrow'
end as "DIFF"   
from BIRTHDAYS
inner join NUMBERS on BIRTHDAYS.LINKED_NUMBER = NUMBERS.CONTACT_NUMBER
where
date_format(BIRTHDAYS.BIRTHDATE,"%m-%d") = date_format(today,"%m-%d")
or
date_format(BIRTHDAYS.BIRTHDATE,"%m-%d") 
= date_format(date_add(today, interval 14 day),"%m-%d")
or
date_format(BIRTHDAYS.BIRTHDATE,"%m-%d") 
= date_format(date_add(today, interval 7 day),"%m-%d")
or
date_format(BIRTHDAYS.BIRTHDATE,"%m-%d") 
= date_format(date_add(today, interval 1 day),"%m-%d");

END$$
DELIMITER ;


