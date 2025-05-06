-- Add DEFAULT CURRENT_TIMESTAMP to admin_registration_key.created_at
ALTER TABLE admin_registration_key
MODIFY created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

-- Add DEFAULT CURRENT_TIMESTAMP to email_confirmaiation_key.created_at
ALTER TABLE email_confirmaiation_key
MODIFY created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

-- Add DEFAULT CURRENT_TIMESTAMP to change_password_key.created_at
ALTER TABLE change_password_key
MODIFY created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

-- Add DEFAULT CURRENT_TIMESTAMP to household_invite.created_at
ALTER TABLE household_invite
MODIFY created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

-- Add DEFAULT CURRENT_TIMESTAMP to group_invite.created_at
ALTER TABLE group_invite
MODIFY created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

-- Add DEFAULT CURRENT_TIMESTAMP to article.created_at
ALTER TABLE article
MODIFY created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;