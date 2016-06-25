# Wifi-Quiz-Orgainiser
An android application that will help organise small quiz events with the help of android built in wifi hotspot functionality.

There are three primary Modules in the application:

1. The host
    The host is the quiz host which creates hotspot and selects question and sends to the participants

2. The participant
    The participant is the one who connects to the hotspot that the host created and waits for receiving questions.

3. Question Store
    The Question Store is Question Management System which helps user to manipulate question in the database.

# How the connection is established between Host and Participant:
I have used java's tcp programming. But in this case the Host instatiates the connection from the host device.
Which is inturn accepted by participant sides ServerSocket object.
Note : This idea of mine(instantiating from the host side migt be a total mess up) but I didn't found a way to it.

# Final Notes
This is the first ever software thing I ever created so I might have made unprofessional mistakes.
Sorry for that, I will appreciate if you point them out.

Thank you for showing your interest.




