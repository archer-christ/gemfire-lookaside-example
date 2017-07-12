# Lookaside caching with Gemfire #

## Getting started ##

```
    git clone https://github.com/gshaw-pivotal/gemfire-lookaside-example.git
```

## Steps to run example ##

These steps are provided in order to run Gemfire and this example on your local machine.

1. Install Gemfire. Instructions for installing can be found on the [Pivotal Gemfire page](http://gemfire.docs.pivotal.io/gemfire/getting_started/installation/install_intro.html). For a OSX machine install can be as simple as the following:

```
    brew untap pivotal/tap
    brew tap pivotal/tap 
    brew install gemfire
```

2. Create an account which can access the Pivotal Commercial Maven Repository. [Registration](https://commercial-repo.pivotal.io/login/auth) is needed in order to obtain geode-core, which our example app uses. [Instructions](http://gemfire.docs.pivotal.io/gemfire/getting_started/installation/obtain_gemfire_maven.html) are available.

3. Add your credentials for the Pivotal Commercial Maven Repository to a gradle.properties file. Do not check these in to any source control.

```
    gemfireReleaseRepoUser=USERNAME
    gemfireReleaseRepoPassword=PASSWORD
```

4. Build the example using

```
    ./gradlew build
```

5. Start the locator and server(s) via

```
    gfsh run --file=scripts/start.gfsh
```

6. Start the example Java app via

```
    ./gradlew run
```

7. Make HTTP GET requests to the application; for example using curl you can do the following

```
    curl -i -H "Accept: application/json" -H "Content-Type: application/json" -X GET http://localhost:8080/music?artist=insertArtistName
```

- Artist names are case sensitive and must match exactly in order to be found in the cache and/or repository. By default our example populates the repository with the following:
    - Bach
    - Mozart
    - Beethoven

8. Shut down the locator and server(s) via

```
    gfsh run --file=scripts/stop.gfsh
```

- It is important to run the stop script when finished, otherwise there can be issues if the start script is run consecutively.