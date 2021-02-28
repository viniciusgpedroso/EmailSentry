# EmailSentry
Tool to get emails of the students in classes available in the 
[DAC website](https://www.dac.unicamp.br/).
Using the update commands the user can get the students that only got in the class after
the alteration period.

## Arguments
Available program arguments:
``--gn [class], --get-new [class]``: Get new class info

``--e [class], --emails [class]``: Get the e-mails of all the current students of a given class

``--le [class], --last-emails [class]``: Get the e-mails from the last update of a given class

``--u [class], --update [class]``: Updates the info about a single class

``--ua, --update-all``: Updates all stored classes and students info

``--exit``: Exits the program

Where ``[class]`` has the following structure:
``[sigla] [disciplina] [turma] ([gradpos] [periodo] [ano])?``

``([gradpos] [periodo] [ano])`` are optional and the default values are:
- ``gradpos: "grad"`` undegraduate classes
- ``periodo`` the current semester (1 from january until june and 2 afterwards)
- ``ano`` the current year

## Samples
``--gn mc 833 a``

``--e mc 833 a``

``--le mc 833 a``