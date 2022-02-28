CREATE TABLE PROBLEM(
                        ID BIGSERIAL NOT NULL AUTO_INCREMENT PRIMARY KEY,
                        DESCRIPTION VARCHAR NOT NULL
);
CREATE TABLE WORSENING(
                          ID BIGSERIAL NOT NULL AUTO_INCREMENT PRIMARY KEY,
                          DESCRIPTION VARCHAR NOT NULL,
                          PROBLEM_ID INT NOT NULL,
                          CONSTRAINT FK_WORSENING_PROBLEM FOREIGN KEY(PROBLEM_ID) REFERENCES PROBLEM(ID)
);
CREATE TABLE SOLUTIONS(
                          ID BIGSERIAL NOT NULL AUTO_INCREMENT PRIMARY KEY,
                          DESCRIPTION VARCHAR NOT NULL,
                          WORSENING_ID INT NOT NULL,
                          CONSTRAINT FK_SOLUTIONS_WORSENING FOREIGN KEY(WORSENING_ID) REFERENCES WORSENING(ID)
);
