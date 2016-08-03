#!/bin/sh

#### BEGIN CONFIGURATION ####

# set dates for backup rotation
NOWDATE=`date +%Y-%m-%d`
LASTDATE=$(date +%Y-%m-%d --date='7 days ago')

# set backup directory variables
SRCDIR='/tmp/turboprodbbackups'

# database access details
HOST='192.168.43.204'
USER='root'
PASS='rootpass@2009'
DB='BacheCompany'

### FTP server Setup ###
FTPD="/backup/turbopro-daily-backups"
FTPU="sysvine"
FTPP="sysvine"
FTPS="sysvine.sysvine.vine"

#### END CONFIGURATION ####

# make the temp directory if it doesn't exist
#mkdir -p $SRCDIR

# dump  database to its own sql file
mysqldump -u $USER -p$PASS -h $HOST BacheCompany > $SRCDIR/$DB.sql

# tar all the databases into $NOWDATE-backups.tar.gz
cd $SRCDIR
tar -czPf $NOWDATE-backup.tar.gz *.sql

# Store backup file into FTP server directory
/usr/bin/lftp -u $FTPU,$FTPP -e "cd $FTPD; mput $SRCDIR/$NOWDATE-backup.tar.gz; quit" $FTPS

# Remove backup file from FTP server directory
#/usr/bin/lftp -e "rm ${FTPD}/${LASTDATE}-backup.tar.gz; bye" -u $FTPU,$FTPP $FTPS

# remove all files in our source directory
rm -f $SRCDIR/*