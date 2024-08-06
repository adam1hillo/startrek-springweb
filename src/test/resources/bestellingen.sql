insert into bestellingen (werknemerId, omschrijving, bedrag, moment)
values ((select id from werknemers where voornaam = 'test1'), 'test1', 10, now()),
       ((select id from werknemers where voornaam = 'test1'), 'test2', 20, now()),
       ((select id from werknemers where voornaam = 'test2'), 'test3', 10, now());