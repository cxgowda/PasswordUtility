console.log("script.js loaded");

function signup() {
        const user = document.getElementById("username").value;
        const pass = document.getElementById("password").value;
        const confirm = document.getElementById("confirm").value;

        // Prepare POST body as form data
        const params = new URLSearchParams();
        params.append("user", user);
        params.append("pass", pass);
        params.append("confirm", confirm);

        fetch("http://localhost:8080/api/signup", {
            method: "POST",
            body: params
        })
        .then(res => res.text())
        .then(data => {
            data = data.trim();
            document.getElementById("output").innerText = data;

            // Redirect to login page if signup is successful
            if (data.startsWith("✅ Signup Successful")) {
                setTimeout(() => {
                    window.location.href = "login.html";
                }, 1000);
            }
        })
        .catch(err => {
            console.error("Signup error:", err);
            document.getElementById("output").innerText = "❌ Something went wrong!";
        });
    }
// ---------------- PASSWORD GENERATOR WITH RULES ----------------
function generatePassword(options = {}) {
    // Allowed Length: 16–24 | If user does not give, random between range
    let length = options.length || Math.floor(Math.random() * (24 - 16 + 1)) + 16;

    // Flags (true by default unless disabled)
    let includeUpper = options.uppercase !== false;
    let includeLower = options.lowercase !== false;
    let includeNumbers = options.numbers !== false;
    let includeSymbols = options.symbols !== false;

    // Character pools
    const upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    const lower = "abcdefghijklmnopqrstuvwxyz";
    const numbers = "0123456789";
    const symbols = "!@#$%^&*()_+-={}[]<>?/|~";

    // Selected characters to build from
    let allowedChars = "";
    let password = [];

    // Ensure at least one char from each enabled category
    if (includeUpper) {
        allowedChars += upper;
        password.push(upper[Math.floor(Math.random() * upper.length)]);
    }
    if (includeLower) {
        allowedChars += lower;
        password.push(lower[Math.floor(Math.random() * lower.length)]);
    }
    if (includeNumbers) {
        allowedChars += numbers;
        password.push(numbers[Math.floor(Math.random() * numbers.length)]);
    }
    if (includeSymbols) {
        allowedChars += symbols;
        password.push(symbols[Math.floor(Math.random() * symbols.length)]);
    }

    // Fill remaining characters randomly
    while (password.length < length) {
        password.push(allowedChars[Math.floor(Math.random() * allowedChars.length)]);
    }

    // Shuffle for randomness
    password.sort(() => Math.random() - 0.5);

    return password.join("");
}


// ---------------- BUTTON HANDLER ----------------
function generate() {
    const password = generatePassword({
        length: 20,      // ← You can change or remove to randomize length
        symbols: true,
        numbers: true,
        uppercase: true,
        lowercase: true
    });

    document.getElementById("password").value = password;
    document.getElementById("confirm").value = password;
}

document.getElementById("toggle").addEventListener("click", function () {
    let input = document.getElementById("password");

    if (input.type === "password") {
        input.type = "text";
        this.textContent = "<o>"; // change icon when visible
    } else {
        input.type = "password";
        this.textContent = "<o>"; // revert icon when hidden
    }
});

async function sendToSlack(password) {
    await fetch("/send-password", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ password })
    });
}

function generate() {
    const pwd = generatePassword({ length: 20 });
    document.getElementById("password").value = pwd;

    sendToSlack(pwd); // 🔐 sent to Slack
}


