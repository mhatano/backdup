# backdup

BackupDuplicated program will create copy of the specified file with "_YYYYMMDDhhmm" format suffix to the filename body. (Before its extension.)

This would be convenient when you cannot trust your file backup system. Create backup file with such format for keeping track of your work.

Some of the text editors support this type of backup file creation at overwriting existing files, but, this would support all other file formats.

Multiple arguments are treated as filenames and processed each.

If already the same name _YYYYMMDDhhmm file exists, newly created backup would add _001 type extra suffix. (In most cases same name backup would hold the same contents, but just in case.)
