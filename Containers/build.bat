: Build the image
:: TODO
call ./gradlew clean build
:: Release on Heroku
:: TODO
 docker build -t registry.heroku.com/my-java-app-for-artemis/web .
 docker push registry.heroku.com/my-java-app-for-artemis/web
 heroku container:release web -a my-java-app-for-artemis
:: Optional: Remove the image locally
:: call docker rmi <image tag>
docker rmi registry.heroku.com/my-java-app-for-artemis/web