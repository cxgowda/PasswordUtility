import fetch from "node-fetch";
import express from "express";
import dotenv from "dotenv";

dotenv.config();
const app = express();

app.use(express.json());

// Endpoint the frontend calls
app.post("/send-password", async (req, res) => {
    const { password } = req.body;

    if (!password) return res.status(400).send("Password required");

    try {
        await fetch(process.env.SLACK_WEBHOOK_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                text: `🔐 *New Secure Password Generated:*\n\`\`\`${password}\`\`\``,
            })
        });

        res.send("Password sent securely to Slack.");
    } catch (err) {
        res.status(500).send("Failed sending to Slack.");
    }
});

app.listen(3000, () => console.log("Server running on port 3000"));
