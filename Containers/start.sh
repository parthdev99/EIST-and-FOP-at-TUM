# Do not change this script unless you know what you are doing
# This script is responsible for setting the required environment variables
# and then starting the app.jar Spring Boot application.

# Download script to create JDBC environment variables
wget https://raw.githubusercontent.com/heroku/heroku-buildpack-jvm-common/main/opt/jdbc.sh
chmod 770 ./jdbc.sh
# Append server start command to script
echo "java -Dserver.port=\"$PORT\" -jar /app/app.jar" >> jdbc.sh
./jdbc.sh
