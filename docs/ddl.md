## Data Definition Language (DDL)

```sqlite
CREATE TABLE IF NOT EXISTS `Source`
(
    `source_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    `name`      TEXT                              NOT NULL COLLATE NOCASE
);

CREATE UNIQUE INDEX IF NOT EXISTS `index_Source_name` ON `Source` (`name`);

CREATE TABLE IF NOT EXISTS `Quote`
(
    `quote_id`  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    `created`   INTEGER,
    `source_id` INTEGER,
    `text`      TEXT                              NOT NULL COLLATE NOCASE,
    FOREIGN KEY (`source_id`) REFERENCES `Source` (`source_id`) ON UPDATE NO ACTION ON DELETE SET NULL
);

CREATE INDEX IF NOT EXISTS `index_Quote_source_id` ON `Quote` (`source_id`);
```

[`ddl.sql`](sql/ddl.sql)