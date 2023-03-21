# System design

## Objectives

- Avoid false positives
- Handle as many alerts as possible
- Handle the alert with more accurate and fastest as possible 
- A discreet service

## Functional requirements

### General

- Request permissions
  - ACCESS_FINE_LOCATION
  - READ_CONTACTS
  - SEND_SMS
- Handling the required permissions
- Saving the images in the internal memory

### Alert service

- If no contact has been selected yet, request that you choose a contact to enable the alert service
- Check the location when the alert service is triggered
- Check the alert service if it is active
- Enable / disable alert service
- From the notification created you can stop the alert
- Put a counter when alert was triggered
- If the alert was triggered in the notifications you can cancel it
- When the alert is triggered vibrate and send messages to all contacts highlighted(personal/location)

### Alerts

- If no alert has been triggered yet, put a message centered on the screen
- If there is no contact in the alert, put a message centered on the screen
- Show alert list
- See alert detail[date, time, location]
- Show alert contact list[contact name, message sent, status]
- Delete alert
- Check if the message was sent or not [status]

### Contacts

- If no contact has been added yet, put a message centered on the screen
- Show contact list
- Add contact
- Import contact
- See contact details
- Edit contact
- Edit profile image
- Delete profile image
- Delete contact
- Enable / disable as a trusted contact
- Edit the default message of the message that is to be sent in the specific contact

### Settings

- Select the sensibility of the alert service
- select if you wish to send the message with the location
- Edit the default message of the message that is to be sent
- Check out the policies of the application
- Check out the application information
- Check out the application help 
- Clean up the data of the whole application

### Upcoming

- Widget with trigger the alert service
- Insert advertisements
- Exporting the alert list
- Put all contacts of the police
- Record for a period of time audio/video
- Call the emergency services immediately

<!-- Onboarding -->
- Create an illustrated sequence of how the application works if it was entered for the first time (gifs/video)
- Create guided user instructions if you enter for the first time
- Create loading screen at app startup
