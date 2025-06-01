# Notes

- RBAC will be used for tokens per project, I.E main token is used to check if user is who he says is, then the roles 
will be checked in the db if the user can do the action that he requested.
- Main Token and Refresh Token will be given to the user upon signing up/signing in, this is one with oauth from google

# OAuth token (google)
- used for singing up/signing in

# Main Token
- 1 day length
- 
* fields
- sub username (identifier)
- aud audience (client id)
- azp Authorized party
- iat issued at
- exp expiration date
- iss issuer
- typ (JWT always)
- token_type {access} (type of token, don't use typ for this)

# Refresh Token
- max token length 365 days (first_issue_date should be used when regenerating refresh tokens to check for this)
- if token can't be regenerated special code should be returned and the user prompted to re-authenticate
- refresh token length should be 30 days
- refresh token should be attempted to be regenerated if its 2 weeks before its expiration date 
- first_issue_date should not change if refresh token is refreshed with a refresh token
* fields
- sub username (identifier)
- aud audience (client id)
- azp Authorized party 
- first_issue_date (the date when the first refresh token was issued)
- refresh_after (if this time has passed try to refresh the refresh token)
- iat issued at
- exp expiration date
- iss issuer
- typ (JWT always)
- token_type {refresh} (type of token, don't use typ for this)
