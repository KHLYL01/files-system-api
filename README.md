# Files System


# A set of files that a group of people are working on and we want to ensure that two people do not write in parallel to the same file.

##
- A web-based system that enables users to add their files to it and place the files in a status of either free or in use and reserved for a certain user.
- Organize files into groups so that the user cannot access all files, but only files belonging to groups that he has the right to access.
- The system allows the user to create a group and invite other users by sending an invitation from a list containing all users and the ability to search for a specific user within it.
- Other members can browse the groups they have joined and add new files after the group creator approves them.
- The basic use cases are check-in and check-out. The first allows a user, after browsing the files, to reserve a free file for himself, download it and modify it, while the second method allows the user to return the modified file, replace the old file and return it to a free state.
- In the check-out process, the user must upload a new file with the same name and extension as the file he reserved.
- The user can select more than one file and perform the check-in process and the system must ensure that all of them are free before reserving them either all or none.
- The system ensures that two users cannot reserve the same file at the same time.
- Estimate reports of reservations and releases according to the file or user.
- Support automatic file backup.
- Notification system.
- Tracing && Logging.
- Allow users to export process reports to CSV files for easy later analysis.
- Compare the uploaded file with the previous file, store the process that was done and the difference, and export a report according to the file or user.
- The system ensures that 100 users can work in parallel together.


###
-The process of sending notifications and the process of comparing files is done asynchronously in the background.

