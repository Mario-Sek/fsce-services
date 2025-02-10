alter table results
    alter column pdf_bytes type bytea USING pdf_bytes::TEXT::BYTEA;