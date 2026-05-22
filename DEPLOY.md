# Deploy from a fresh Mac (no Java / Docker / Maven needed)

You only need:
- `git` (already installed on your Mac)
- A free **GitHub** account: https://github.com/join
- A free **Render** account: https://render.com/register (use "Sign in with GitHub")

Render builds the Docker images on its side — your machine never compiles Java.

---

## Step 1 — Push the project to GitHub

```bash
cd /Users/manjunathbennur/Desktop/Manjunath/AI/test
git init
git add .
git commit -m "Initial commit: Java microservices demo"
git branch -M main
```

Now create an empty repo on GitHub:
1. Open https://github.com/new
2. Name it: `name-microservice` (any name is fine)
3. **Do not** add a README, .gitignore, or license
4. Click **Create repository**
5. On the next page, copy the two lines under **"…or push an existing repository"** and run them in your terminal. They look like:

```bash
git remote add origin https://github.com/<your-username>/name-microservice.git
git push -u origin main
```

GitHub may ask you to log in — when it asks for a password, use a **Personal Access Token** instead (https://github.com/settings/tokens → Generate new token (classic) → check `repo` → copy the token).

---

## Step 2 — Deploy to Render with the Blueprint

1. Go to https://dashboard.render.com/blueprints
2. Click **New Blueprint Instance**
3. Connect your GitHub account, then pick `name-microservice`
4. Render reads [render.yaml](render.yaml) and shows two services:
   - `name-service`
   - `web-service`
5. For `web-service`, the env var `NAME_SERVICE_URL` will be marked **"Add value"** — leave it blank for now and click **Apply**.
6. Wait ~5 minutes for both services to build. Watch the logs in the Render dashboard.

---

## Step 3 — Connect the two services

After both services show **Live**:

1. Open the `name-service` page in Render. Copy its public URL (top of the page), e.g.:
   ```
   https://name-service-abcd.onrender.com
   ```
2. Open the `web-service` page → **Environment** tab on the left.
3. Edit `NAME_SERVICE_URL` and paste the URL from step 1. Click **Save Changes**.
4. Render redeploys `web-service` automatically (~1 min).

---

## Step 4 — Try it

Open the public URL of `web-service` in your browser, e.g.:
```
https://web-service-abcd.onrender.com
```
Enter a first and last name → Submit. You should see a greeting + JSON response.

---

## Notes about the free tier
- Services **sleep after ~15 minutes** of no traffic. The first request after sleep cold-starts in ~30 seconds — be patient.
- Free tier gives 750 instance-hours/month, shared across services.
- If a build fails, click into the Render service → **Logs** tab → scroll to the error and paste it back to me.

---

## Updating the app later
After any code change:
```bash
git add .
git commit -m "your message"
git push
```
Render auto-detects the push and redeploys.
