CREATE TABLE credit_applications (
                                     id BIGSERIAL PRIMARY KEY,
                                     amount DECIMAL NOT NULL,
                                     term INTEGER NOT NULL,
                                     income DECIMAL NOT NULL,
                                     current_credit_load DECIMAL NOT NULL,
                                     credit_rating INTEGER NOT NULL,
                                     status VARCHAR(20) NOT NULL
);