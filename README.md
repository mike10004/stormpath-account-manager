# stormpath-account-manager

Web application hosted on Google App Engine that allows users from a Google 
Apps Domain to reset the password of an account managed with Stormpath.

## Required configuration

### Create a Google Cloud project

Created a Google Cloud project and create OAuth credentials for a web 
application. Set the authorized redirect URLs to 

* https://your-app-id.appspot.com/callback?client_name=GoogleAppsDomainClient and
* http://localhost:8080/callback?client_name=GoogleAppsDomainClient

Note the client ID and client secret. You will need them below.

### Create a Stormpath application

Create a [Stormpath](https://stormpath.com/) application. You'll need your 
developer API key and the application ID below.

### Maven user settings

Set the following properties in a Maven profile that is activated during build:

* stormpath-account-manager.application.apiKey.id - Stormpath developer API key ID
* stormpath-account-manager.application.apiKey.secret - Stormpath developer API key secret
* stormpath-account-manager.application.id - Stormpath application ID 
* stormpath-account-manager.appengine.app.id - App Engine app ID
* stormpath-account-manager.project.name - display name for your project
* stormpath-account-manager.shiro.google.domain - Google Apps Domain to which
  authentication to this web app is to be restricted
* stormpath-account-manager.shiro.google.client.id - Google Cloud project
  client ID
* stormpath-account-manager.shiro.google.client.secret - Google Cloud project
  client secret
* stormpath-account-manager.shiro.google.redirectUri.remote - URL that corresponds
  to the appspot-hosted authorized redirect URI for your Google Cloud project,
  without the query part; i.e. https://your-app-id.appspot.com/callback

### Use JDK 7 to build

You may run into class version problems if you use JDK 8. Project has been
built and deployed successfully with JDK 7.

## Why you might want to deploy this application

The conditions that would make this application useful for you are:

1. you have a web application in which Stormpath provides the authentication 
   back end
2. users that should be allowed to reset their passwords all have accounts in 
  a Google Apps Domain
3. you can host an app on App Engine (with billing enabled)

If only condition 1 is true, but you still want to allow users to reset their
passwords

## Why this application was built

I had a web application that customers would deploy in various environments,
plugging in different mechanisms for authentication. For one of these 
deployments, I wanted to use Stormpath for authentication, but I didn't want to 
pollute the codebase with Stormpath-specific code for the password reset 
workflow. Luckily, the users that would need to be able to manage their own
credentials were all part of a single Google Apps Domain. So those users
authenticate with their Google account to this web application, which enables
them to reset the password on their Stormpath-managed account. Essentially,
this app operates as a sort of admin console for the other application.
