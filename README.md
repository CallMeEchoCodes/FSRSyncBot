# FurfSky Role Sync Bot

<img src=".github/assets/javacord.svg" alt="Built with Javacord" />

<h3></h3>

## 📔 About

Simple role syncing bot, made for FurfSky Reborn.

## Usage

Add a config.json to the bots working directory with the following contents:
```json
{
	"token": "<YOUR_BOT_TOKEN>",
	"guild_id": <GUILD_TO_SYNC_FROM>,
	"sync_guild_id": <GUILD_TO_SYNC_TO>,
	"guild_role_id": <ROLE_TO_SYNC_FROM>,
	"sync_guild_role_id": <ROLE_TO_SYNC_TO>,
	"debug": false,
	"channel_id": <CHANNEL_FOR_ERROR_MESSAGE>
}
```
Make sure the bot has a role above the sync role and has permission to add roles.
