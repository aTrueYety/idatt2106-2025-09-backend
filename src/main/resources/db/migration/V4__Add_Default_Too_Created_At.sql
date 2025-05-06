-- Add DEFAULT CURRENT_TIMESTAMP to admin_registration_key.created_at
ALTER TABLE admin_registration_key
${alter_column} created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

-- Add DEFAULT CURRENT_TIMESTAMP to email_confirmaiation_key.created_at
ALTER TABLE email_confirmaiation_key
${alter_column} created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

-- Add DEFAULT CURRENT_TIMESTAMP to change_password_key.created_at
ALTER TABLE change_password_key
${alter_column} created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

-- Add DEFAULT CURRENT_TIMESTAMP to household_invite.created_at
ALTER TABLE household_invite
${alter_column} created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

-- Add DEFAULT CURRENT_TIMESTAMP to group_invite.created_at
ALTER TABLE group_invite
${alter_column} created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

-- Add DEFAULT CURRENT_TIMESTAMP to article.created_at
ALTER TABLE article
${alter_column} created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;