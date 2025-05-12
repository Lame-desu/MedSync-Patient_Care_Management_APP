require("dotenv").config();
const express = require("express");
const mongoose = require("mongoose");
const cors = require("cors");
const bodyParser = require("body-parser");
const authRoutes = require("./routes/authRoutes");
const doctorRoutes = require("./routes/doctorRoutes");
const triageRoutes = require("./routes/triageRoutes");
const adminRoutes = require("./routes/adminRoutes");
const patientRoutes = require("./routes/patientRoutes");

const app = express();

app.use(cors());
app.use(bodyParser.json());
