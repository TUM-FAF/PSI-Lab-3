import datetime
import MySQLdb

class AbstractTransactionManager(object):
	def __init__(self):
		raise NotImplementedError

	def StoreTransaction(self, transaction):
		raise NotImplementedError

	def GetTransactionById(self, transactionId):
		raise NotImplementedError

	def ToggleTransactionStatus(self, transactionId):
		raise NotImplementedError

	def CreateDb(self):
		raise NotImplementedError

"""
DROP TABLE IF EXISTS transactions CASCADE;

CREATE TABLE transactions (
	id BIGINT(99) NOT NULL AUTO_INCREMENT,
	originalId TEXT,
	phoneNumber TEXT,
	apId TEXT,
	originalRequest LONGTEXT,
	transformedRequest LONGTEXT,
	ttimestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	sstatus INTEGER,
	UNIQUE KEY (id, ttimestamp) )
PARTITION BY RANGE (UNIX_TIMESTAMP(ttimestamp)) (
	PARTITION transactions_01_2012 VALUES LESS THAN (UNIX_TIMESTAMP('2012-01-01 00:00:00')) );

ALTER TABLE transactions ADD PARTITION ( PARTITION transactions_09_2012 VALUES LESS THAN ( UNIX_TIMESTAMP('2012-10-01 00:00:00') ) );

ALTER TABLE transactions ADD PARTITION ( PARTITION transactions_10_2012 VALUES LESS THAN ( UNIX_TIMESTAMP('2012-11-01 00:00:00') ) );

ALTER TABLE transactions ADD PARTITION ( PARTITION transactions_11_2012 VALUES LESS THAN ( UNIX_TIMESTAMP('2012-12-01 00:00:00') ) );

INSERT INTO transactions VALUES ( NULL, '123', '456', '789', '<foo_source>...</foo_source>', '<foo_updated>...</foo_updated>',CURRENT_TIMESTAMP,0);

SELECT * FROM transactions;

SELECT * FROM transactions WHERE ttimestamp BETWEEN '2012-11-01 00:00:00' and '2012-12-01 00:00:00';

ALTER TABLE transactions DROP PARTITION transactions_11_2012;
"""

STATUS_PENDING = 0
STATUS_PROCESSED = 1

class MySQLTransactionManager(AbstractTransactionManager):
	def __init__(self):
		self.Connect()

	def __del__(self):
		self.Close()

	def Connect(self):
		try:
			for i in range(3):
				try:
					self.conn = MySQLdb.connect(host='localhost', user='root', passwd='123456', db='test')
					self.curs = self.conn.cursor()
					break
				except MySQLdb.OperationalError, err:
					raise RuntimeError (err)
		except:
			err = 'No servers to connect to!'
			raise RuntimeError (err)

	def Close(self):
		print 'Closing MySQL connection'
		self.curs.close()
		self.conn.close()

	def CreateDB(self):
		try:
			#self.curs.execute("""DROP TABLE IF EXISTS transactions CASCADE;""")
			self.curs.execute("""
				CREATE TABLE transactions (
					id					BIGINT(99) NOT NULL AUTO_INCREMENT,
					originalId			TEXT,
					phoneNumber			TEXT,
					apId				TEXT,
					originalRequest		LONGTEXT,
					transformedRequest	LONGTEXT,
					ttimestamp			TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
					sstatus				INTEGER,
					UNIQUE KEY (id, ttimestamp) )
				PARTITION BY RANGE (UNIX_TIMESTAMP(ttimestamp)) (
					PARTITION transactions_01_2000 VALUES LESS THAN (UNIX_TIMESTAMP('2000-01-01 00:00:00')) );
				""")
			self.conn.commit()

		except Exception, err:
			raise RuntimeError (err)

	def PartitionDB(self, year = None, month = None):
		if not year:
			year = datetime.date.today().year
		if not month:
			month = datetime.date.today().month

		if month == 12:
			year_interval = year + 1
			month_interval = 1
		else:
			year_interval = year
			month_interval = month + 1

		try:
			self.curs.execute("""
				ALTER TABLE transactions ADD PARTITION (
					PARTITION transactions_%s_%s VALUES LESS THAN ( UNIX_TIMESTAMP('%s-%s-01 00:00:00') ) );
				""", (month, year, year_interval, month_interval))
			self.conn.commit()

		except Exception, err:
			raise RuntimeError (err)

	def StoreTransaction(self, t):
		for i in range(3):
			try:
				self.curs.execute("""
						INSERT INTO transactions
						VALUES (NULL,
						%s,%s,%s,%s,%s,CURRENT_TIMESTAMP,%s);""",
						(t['originalId'],
						t['phoneNumber'],
						t['apId'],
						t['originalRequest'],
						t['transformedRequest'],
						STATUS_PENDING,))
				self.conn.commit()
				return self.curs.lastrowid

			except Exception, err:
				code, msg = err
				if code == 1526:
					self.conn.rollback()
					self.PartitionDB()
				else:
					raise RuntimeError (err)

if __name__ == '__main__':
	mysql = MySQLTransactionManager()
	#mysql.CreateDB()
	t = {'originalId': 12345,
		'phoneNumber': 12345,
		'apId': 2222,
		'originalRequest': '<foo_source>...</foo_source>',
		'transformedRequest': '<foo_updated>...</foo_updated>'}
	print mysql.StoreTransaction(t)