CREATE TABLE IF NOT EXISTS users
(
    id
    BIGSERIAL
    PRIMARY
    KEY,
    email
    VARCHAR
(
    255
) NOT NULL UNIQUE,
    password VARCHAR
(
    255
) NOT NULL,
    role VARCHAR
(
    50
) NOT NULL
    );

CREATE TABLE IF NOT EXISTS documents
(
    id
    BIGSERIAL
    PRIMARY
    KEY,
    title
    VARCHAR
(
    255
) NOT NULL,
    content TEXT,
    status VARCHAR
(
    50
) NOT NULL,
    owner_id BIGINT NOT NULL REFERENCES users
(
    id
)
    );

CREATE TABLE IF NOT EXISTS approvals
(
    id
    BIGSERIAL
    PRIMARY
    KEY,
    document_id
    BIGINT
    NOT
    NULL
    REFERENCES
    documents
(
    id
),
    approver_id BIGINT NOT NULL REFERENCES users
(
    id
),
    status VARCHAR
(
    50
) NOT NULL,
    comment TEXT
    );

CREATE TABLE IF NOT EXISTS audit_logs
(
    id
    BIGSERIAL
    PRIMARY
    KEY,
    action
    VARCHAR
(
    100
) NOT NULL,
    actor VARCHAR
(
    255
) NOT NULL,
    details TEXT,
    timestamp TIMESTAMP NOT NULL
    );

CREATE INDEX IF NOT EXISTS idx_documents_status ON documents (status);
CREATE INDEX IF NOT EXISTS idx_documents_owner_id ON documents (owner_id);
CREATE INDEX IF NOT EXISTS idx_approvals_document ON approvals (document_id);
CREATE INDEX IF NOT EXISTS idx_approvals_approver ON approvals (approver_id);
CREATE INDEX IF NOT EXISTS idx_audit_logs_actor ON audit_logs (actor);
CREATE INDEX IF NOT EXISTS idx_audit_logs_action ON audit_logs (action);
CREATE INDEX IF NOT EXISTS idx_audit_logs_time ON audit_logs (timestamp);