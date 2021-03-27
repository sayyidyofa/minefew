# minefew
Minecraft Server Event Webhook

This is a server-only mod. Fires json post request to external listening http endpoints

### How to use

When launching your server jar, add additional env variables:
 - `HOOK_WHERE_TO_FIRE` http endpoint url
 - `HOOK_AUTH_KEY` auth key, if your endpoint needs one, if not just use empty string
