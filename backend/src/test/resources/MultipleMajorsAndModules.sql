INSERT INTO major_uni (name, visible_choice) VALUES ('B.Sc. Informatik', true);
INSERT INTO module_uni (number, name, visible_choice, major_uni_name) VALUES ('10-201-2012', 'Einführung Programmierung', false, 'B.Sc. Informatik');
INSERT INTO module_uni (number, name, visible_choice, major_uni_name) VALUES ('10-201-2005-2', 'Programmierparadigmen', true, 'B.Sc. Informatik');

INSERT INTO major_uni (name, visible_choice) VALUES ('M.Sc. Informatik', false);
INSERT INTO module_uni (number, name, visible_choice, major_uni_name) VALUES ('', 'Kermodul (Wahlpflichtfach)', false, 'M.Sc. Informatik');