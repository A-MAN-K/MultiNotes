# MultiNotes
Multi Notes
Uses: RecyclerView, CardLayout, Multi-Activity, JSON File, Option-Menus, Async Task
App Requirements
• This app allows the creation and maintenance of multiple notes. Any number of notes are allowed (including no notes at all). Notes are made up of a Title and Note Text.
• There is no need to use a different layout for landscape orientation in this application.
• Notes should be saved to (and loaded from) the internal file system in JSON format. If no file is found upon loading, the application should start with no existing notes and no errors. (A new JSON file would then be created when new notes are saved).
• JSON file loading must happen in an AsyncTask that is started in the onCreate method. Saving should happen in the onPause method.
• A Note class (with last save date and note text) should be created to represent each individual note in the application.
