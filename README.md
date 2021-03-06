<div align="center">
  <h1>example-api</h1>
</div>
<br />

Generated from starterkit version 0.0.1

**In this README:**

- [About](#about)
- [Getting Started](#getting-started)
  - [Required Dependencies](#required-dependencies)
  - [Running the Application](#running-the-application)
  - [Verifying the Application is Running](#verifying-the-application-is-running)
- [CI/CD Pipeline](#cicd-pipeline)
  - [Secrets](#secrets)
  - [Default Pipeline Behavior](#default-pipeline-behavior)
  - [Dependencies](#dependencies)
  - [Deploying the Application](#deploying-the-application)
- [What's Next](#whats-next)

---

## About

This is a Java Spring Boot application that has been created using the [Lighthouse DI Java 17 Starterkit][1].
It is intended to be used as a starting point for building Java APIs and should be customized to deliver whatever functionality is required.
If no other changes have been made, this application will have [these features][2] included by default.

## Getting Started

### Required Dependencies

Before you run this application locally, you will need to make sure you have all the following required dependencies available in your local environment:

- Java 11
    - This must be the version selected by your machine to run Gradle 6.9. [Mac Guide][6]|[Other OS Guide][5]
- Java 16
    - This version must be available to Gradle to compile the application. [Mac Guide][6]|[Other OS Guide][5]
- [docker][7]
- [hadolint][8]
- [spectral][12]
- [shellcheck][9]

> This application currently supports Mac OS for local development environments.
> Use the [Mac OS Guide][4] to make sure you have all the above dependencies available in your local environment.
> Otherwise, refer to the [Other Operating Systems Guide][5].

You will also need to have a GitHub personal access token with the `read:packages` permission exported in your local shell.
This is required to in order to download application artifacts that are published to the [VA GitHub Package Registry][10].

You can generate a new access token by following [this guide][11].
When you have your token, make sure it is available in your local shell by running:

```bash
export GITHUB_ACCESS_TOKEN=<replace-with-token-from-github>
```

#### Install Locations

You may wish to configure Gradle to use a specific install location for required binaries. For example, specifying the full binary path can prevent attacks of the type in which a malicious actor inserts their own corrupted version of the binary ahead of the expected binary in the system path, or you may want to override the default system binary for some reason.

##### hadolint

Add the following in `app/build.gradle`:

```groovy
tasks.named('lintDockerfile').configure {
    ext.binary = '/path/to/hadolint'
}
```

##### ShellCheck

Add the following in `scripts/build.gradle`:

```groovy
shellcheck {
    shellcheckBinary = '/path/to/shellcheck'
}
```

### Running the Application

Once you have all the required dependencies, you can start the application in your local environment by navigating to the root of your application directory and running the following command:

```bash
./gradlew clean
./gradlew build check docker
```

This will build all the application artifacts and docker images.

> Note: Due to the way Gradle computes dependencies, the `clean` command must always be separate from `build` commands

You can then start the application by running:

```bash
./gradlew :app:dockerComposeDown :app:dcPrune :app:dockerComposeUp
```

This should bring up a docker container with the app running at http://localhost:8081

There are shortcut tasks defined in the root `build.gradle` file to make life a bit easier:

```bash
./gradlew devloop  # will rebuild what is out of date, recreate and restart individual docker images
./gradlew restartloop # will stop containers, then proceed with `devloop`
./gradlew resetloop # will stop containers, clean volumes, then proceed with `devloop`
```

The `devloop` is most convenient and quickest feedback cycle if one needs to do manual testing with the system services.

> Note that at this time, `./gradlew run` and `./gradlew bootRun` require additional setup with database dependencies prior to use with a local development environment.

### Verifying the Application is Running

You can verify that the application is up and running by issuing the following commands in your terminal:

```bash
curl localhost:8081/health
curl localhost:8081/info
```

You should get back responses similar to the following:

```bash
curl localhost:8081/health

{
    "status":"UP",
    "components":{
        "db":{
            "status":"UP",
            "details":{
                "database":"PostgreSQL",
                "validationQuery":"isValid()"
            }
        },
        "diskSpace":{
            "status":"UP",
            "details":{
                "total":62725623808,
                "free":53279326208,
                "threshold":10485760,
                "exists":true
            }
        },
        "livenessState":{
            "status":"UP"
        },
        "ping":{
            "status":"UP"
        },
        "readinessState":{
            "status":"UP"
        }
    },
    "groups":[
        "liveness",
        "readiness"
    ]
}
```

```bash
curl localhost:8081/info

{
    "app": {
        "description": "Java API Starter from Template",
        "name": "example-api"
    }
}
```

## CI/CD Pipeline

This project comes with a skeleton Github Actions CI/CD pipeline out of the box. You can always choose to rewrite the pipeline using a different CI/CD tool; this pipeline serves as an example that you can use and run with minimal setup.

### Secrets

In order to run the pipeline, you will need to [create a personal access token][11] and add it to your repository's secrets in Github. The access token should have `write:packages` scope.

The secrets you need to configure are

- `ACCESS_TOKEN`: the personal access token
- `USERNAME`: the Github username of the user who owns the access token

### Default Pipeline Behavior

The default pipeline has 3 jobs, which do the following things:

- Runs CIS benchmark tests against the application Docker image using `docker-bench-security`
- Builds and tests application
- Publishes Docker image to VA GHCR repository

### Dependencies

The pipeline runs on Github's `ubuntu-latest` runner, which is currently Ubuntu 20.04. The Github Actions [Ubuntu 20.04 documentation](https://github.com/actions/virtual-environments/blob/main/images/linux/Ubuntu2004-Readme.md) lists the software installed by default. To learn more about choosing a Github runner and Github-hosted runner types, see the [`job.<job-id>.runs-on`](https://docs.github.com/en/actions/using-workflows/workflow-syntax-for-github-actions#jobsjob_idruns-on) documentation.

Software required for the pipeline but not installed by default, such as Java 17, hadolint, and spectral, is installed in the pipeline. The installation for app build dependencies is implemented as an action in [`./.github/actions/install-build-dependencies/action.yml`](./.github/actions/install-build-dependencies/action.yml).

### Deploying the Application

The pipeline does not currently deploy the application to the DI Kubernetes clusters out of the box, although this setup will be coming in the future. To learn how to deploy your applications, see the [DI ArgoCD docs](https://animated-carnival-57b3e7f5.pages.github.io/cluster-services/argo-cd/).

## Common Errors

1. Error: Cannot find plugin

    Error Message:

    ```
    * What went wrong:
    Plugin [id: 'gov.va.starter.plugin.cookiecutter', version: '0.1.20', apply: false] was not found in any of the following sources:

    - Gradle Core Plugins (plugin is not in 'org.gradle' namespace)
    - Plugin Repositories (could not resolve plugin artifact 'gov.va.starter.plugin.cookiecutter:gov.va.starter.plugin.cookiecutter.gradle.plugin:0.1.20')
    Searched in the following repositories:
        MavenLocal(file:/Users/aasare/.m2/repository/)
        Gradle Central Plugin Repository
        MavenRepo
        BintrayJCenter
        maven(https://palantir.bintray.com/releases)
        maven2(https://dl.bintray.com/adesso/junit-insights)
        starterBootPkgs(https://maven.pkg.github.com/department-of-veterans-affairs/lighthouse-di-starter-boot)
        nexus(https://tools.health.dev-developer.va.gov/nexus)
    ```

    Fix:  Set your Github token as per the instructions in the [Required Dependencies](#required-dependencies) section above.

## What's Next

Once you have verified that you are able to run the application successfully, you can now start customizing the application to deliver the functionality you would like.

By default, this application assumes the use of a build, test, release cycle as defined in [this development guide][14].
Take a look at that guide to see how you can make changes, test them and get them deployed to a target environment.

The application itself is organized into the following three tiers of functionality:

- API
- Service (business logic)
- Persistence

To see how each of these tiers is used by default, take a look at the [Project Structure][13] documentation.

[1]: https://github.com/department-of-veterans-affairs/lighthouse-di-starterkit-java
[2]: https://animated-carnival-57b3e7f5.pages.github.io/starterkits/java/explore-features/
[4]: https://github.com/department-of-veterans-affairs/lighthouse-di-starterkit-java/blob/main/docs/developing-on-mac.md
[5]: https://animated-carnival-57b3e7f5.pages.github.io/starterkits/java/developing-on-other-os/
[6]: https://animated-carnival-57b3e7f5.pages.github.io/starterkits/java/developing-on-mac/
[7]: https://docs.docker.com/get-docker/
[8]: https://github.com/hadolint/hadolint#install
[9]: https://github.com/koalaman/shellcheck#readme
[10]: https://github.com/orgs/department-of-veterans-affairs/packages
[11]: https://docs.github.com/en/github/authenticating-to-github/keeping-your-account-and-data-secure/creating-a-personal-access-token
[12]: https://meta.stoplight.io/docs/spectral/ZG9jOjYyMDc0Mw-installation
[13]: https://github.com/department-of-veterans-affairs/lighthouse-di-starterkit-java/blob/main/docs/project-structure.md
[14]: https://github.com/department-of-veterans-affairs/lighthouse-di-starterkit-java/blob/main/docs/development-guide.md
