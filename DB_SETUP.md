# Add a free PostgreSQL database (Neon)

Neon gives you a free, always-on Postgres database with no credit card required.

## Step 1 — Create the Neon database

1. Go to https://neon.tech and click **Sign Up** (use "Sign up with GitHub" — fastest).
2. After signup, Neon creates your first project automatically. If it asks:
   - **Project name**: `name-microservice`
   - **Postgres version**: latest (default)
   - **Region**: pick the one closest to Render's region (us-west-2 if you used the defaults, otherwise us-east-1)
3. After the project is created, you land on a **Connection Details** screen.

## Step 2 — Copy the connection details

Neon shows a connection string that looks like:
```
postgresql://manjuinusa:AbCdEf123Xyz@ep-cool-frog-12345.us-east-2.aws.neon.tech/neondb?sslmode=require
```

Break that into 3 pieces — you'll need each one for Render:

| Part | Example |
|---|---|
| **Username** | `manjuinusa` (the bit between `postgresql://` and `:`) |
| **Password** | `AbCdEf123Xyz` (between `:` and `@`) |
| **Host + database** | `ep-cool-frog-12345.us-east-2.aws.neon.tech/neondb?sslmode=require` (everything after `@`) |

**Tip**: Neon's UI has a "Show password" toggle and a "Copy" button — use those.

## Step 3 — Set env vars on Render's `name-service`

1. Open https://dashboard.render.com → click **`name-service`**
2. Left sidebar → **Environment**
3. Click **Add Environment Variable** and add these **three** (one at a time):

   | Key | Value |
   |---|---|
   | `SPRING_DATASOURCE_URL` | `jdbc:postgresql://ep-cool-frog-12345.us-east-2.aws.neon.tech/neondb?sslmode=require` ← note the `jdbc:` prefix and **no** username/password in URL |
   | `SPRING_DATASOURCE_USERNAME` | `manjuinusa` (the username from step 2) |
   | `SPRING_DATASOURCE_PASSWORD` | `AbCdEf123Xyz` (the password from step 2) |

   ⚠️ **Important** for `SPRING_DATASOURCE_URL`:
   - Replace `postgresql://` with `jdbc:postgresql://`
   - **Strip** the `username:password@` part — that goes in the separate username/password env vars
   - Keep `?sslmode=require` at the end (Neon needs SSL)

   Example transform:
   ```
   Neon shows:  postgresql://manjuinusa:Abc123@ep-foo.aws.neon.tech/neondb?sslmode=require
   You enter:   jdbc:postgresql://ep-foo.aws.neon.tech/neondb?sslmode=require
   ```

4. Click **Save Changes**. Render redeploys `name-service` (~2 min).

## Step 4 — Verify

Once `name-service` is **Live** again, hit its health endpoint in your browser:
```
https://name-service-XXXX.onrender.com/api/names/health
```
Should return `{"status":"UP","service":"name-service"}` — same as before, but now backed by a real DB.

Then on the frontend (`https://web-service-dtby.onrender.com`):
- Submit a name → it's saved to Neon
- Click **"Show all stored names"** → see everything you've submitted, each with a unique UUID

## Verify in the Neon dashboard (optional)

In the Neon dashboard → **Tables** → you should see a `names` table with columns:
- `id` (uuid)
- `first_name` (varchar)
- `last_name` (varchar)
- `created_at` (timestamp)

Each row corresponds to one form submission.

---

## Troubleshooting

**`name-service` fails to start, logs say "Connection refused" or "could not connect"**
→ The host in `SPRING_DATASOURCE_URL` is wrong, or you forgot the `jdbc:` prefix, or `?sslmode=require` is missing.

**Logs say "password authentication failed"**
→ Username or password env var is wrong. Re-copy from Neon's connection details (use the Show password toggle).

**Logs say "relation \"names\" does not exist"**
→ Should not happen — Hibernate creates the table on startup (`spring.jpa.hibernate.ddl-auto=update`). If it does, check the user has CREATE TABLE permission in Neon (the default Neon user does).
