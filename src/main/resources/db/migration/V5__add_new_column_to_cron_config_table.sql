ALTER TABLE cron_config ADD COLUMN cron_name VARCHAR(25) NOT NULL DEFAULT 'EVERY_SECOND_JOB';

DELETE FROM cron_config WHERE id=2;

INSERT INTO cron_config (id, cron_name, cron_expression) VALUES
                                                             (3, 'EVERY_MINUTE_JOB', '0 * * * * ?'),
                                                              (4, 'EVERY_HOUR_JOB',  '0 0 * * * ?'),
                                                              (5, 'EVERY_DAY_JOB',   '0 0 0 * * ?'),
                                                              (6, 'EVERY_WEEK_JOB',  '0 0 0 * * MON'),
                                                              (7, 'EVERY_MONTH_JOB', '0 0 0 1 * ?');