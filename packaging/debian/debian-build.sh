#!/bin/bash

MOTECH_VERSION=0.11_1
TMP_DIR=/tmp/motech-debian-build-$$
WARNAME=motech-platform-server.war
MOTECH_PACKAGENAME="motech_$MOTECH_VERSION.deb"
MOTECH_BASE_PACKAGENAME="motech-base_$MOTECH_VERSION.deb"
CURRENT_DIR=`pwd`

# exit on non-zero exit code
set -e

# Set motech directory
MOTECH_BASE=../..
if [ $# -gt 0 ]; then
    MOTECH_BASE=$1
fi

cd $MOTECH_BASE
MOTECH_BASE=`pwd`

PACKAGING_DIR=$MOTECH_BASE/packaging/debian
WAR_PATCHDIR=$PACKAGING_DIR/warpatch
MOTECH_WAR=$MOTECH_BASE/motech-platform-server/target/$WARNAME

echo "====================="
echo "Building motech-base"
echo "====================="

if [ ! -f $MOTECH_WAR ]; then
    echo $MOTECH_WAR does not exist
    exit 1
fi

# Create a temp dir for package building
mkdir $TMP_DIR
cp $MOTECH_WAR $TMP_DIR
cd $TMP_DIR

# Create empty dirs if missing
mkdir -p motech-base/var/cache/motech/work/Catalina/localhost
mkdir -p motech-base/var/cache/motech/temp
mkdir -p motech-base/var/cache/motech/felix-cache
mkdir -p motech-base/var/lib/motech/webapps
mkdir -p motech-base/var/log/motech
mkdir -p motech-base/usr/share/motech/.motech/bundles
mkdir -p motech-base/usr/share/motech/.motech/rules

# copy motech-base
cp -r $PACKAGING_DIR/motech-base .
mv $WARNAME ./motech-base/var/lib/motech/webapps/

# set up permissions
find ./motech-base -type d | xargs chmod 755  # for directories
find ./motech-base -type f | xargs chmod 644  # for files
# special permissions for executbale files
chmod 755 ./motech-base/DEBIAN/postinst
chmod 755 ./motech-base/DEBIAN/prerm
chmod 755 ./motech-base/DEBIAN/postrm
chmod 755 ./motech-base/DEBIAN/control
chmod 755 ./motech-base/etc/init.d/motech

# Build package
echo "Building package"
fakeroot dpkg-deb --build motech-base

if [ ! -d $PACKAGING_DIR/target ]; then
    mkdir $PACKAGING_DIR/target
fi

mv motech-base.deb $PACKAGING_DIR/target/$MOTECH_BASE_PACKAGENAME

# Check package for problems
echo "Checking package with lintian"
lintian -i $PACKAGING_DIR/target/$MOTECH_BASE_PACKAGENAME

echo "Done! Created $MOTECH_PACKAGENAME"

#clean up
rm -r $TMP_DIR/*

echo "====================="
echo "Building motech"
echo "====================="

# copy files
cp -r $PACKAGING_DIR/motech .

# set up permissions
find ./motech -type d | xargs chmod 755  # for directories
find ./motech -type f | xargs chmod 644  # for files
# special permissions for executbale files
chmod 755 ./motech/DEBIAN/control

echo "Building package"

fakeroot dpkg-deb --build motech
mv motech.deb $PACKAGING_DIR/target/$MOTECH_PACKAGENAME

echo "Checking package with lintian"
lintian -i $PACKAGING_DIR/target/$MOTECH_PACKAGENAME

echo "Done! Created $MOTECH_PACKAGENAME"

# clean up
cd $CURRENT_DIR
rm -r $TMP_DIR

# build modules
export MOTECH_BASE
$PACKAGING_DIR/modules/build-modules.sh