--books
INSERT INTO books (title, author, isbn,
                   publication_year, total_copies, available_copies)
VALUES ('Clean Code', 'Robert C. Martin', '9780132350884',
        2009, 1, 0);
INSERT INTO books (title, author, isbn,
                   publication_year, total_copies, available_copies)
VALUES ('Grokking Algorithms', 'Aditya Y. Bhargava', '9781633438538',
        2024, 10, 9);
INSERT INTO books (title, author, isbn,
                   publication_year, total_copies, available_copies)
VALUES ('Spring in Action', ' Craig Walls', '9781617297571',
        2022, 5, 4);

--readers
INSERT INTO readers (first_name, last_name, email, phone, registration_date)
VALUES ('Jon', 'Snow', 'snow@gmail.com',
        '+19991234567', '2026-01-01');
INSERT INTO readers (first_name, last_name, email, phone, registration_date)
VALUES ('Eddard', 'Stark', 'ned@gmail.com',
        '+19997775533', '2026-02-02');

--loans
INSERT INTO loans (book_id, reader_id, loan_date, due_date, return_date, status)
VALUES (1, 1, '2026-01-01',
        '2026-01-15', NULL, 'OVERDUE');
INSERT INTO loans (book_id, reader_id, loan_date, due_date, return_date, status)
VALUES (2, 1, '2026-06-01',
        '2026-06-15', '2026-06-10', 'RETURNED');
INSERT INTO loans (book_id, reader_id, loan_date, due_date, return_date, status)
VALUES (3, 1, '2026-06-01',
        '2026-08-01', NULL, 'ISSUED');
INSERT INTO loans (book_id, reader_id, loan_date, due_date, return_date, status)
VALUES (2, 2, '2026-06-15',
        '2026-07-15', NULL, 'ISSUED');
