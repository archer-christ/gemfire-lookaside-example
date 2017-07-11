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

7. Shut down the locator and server(s) via

```
    gfsh run --file=scripts/stop.gfsh
```