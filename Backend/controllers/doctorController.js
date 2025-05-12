const MedicalHistory = require('../models/medicalHistoryModel');
const Prescription = require('../models/prescriptionModel');
const Appointment = require('../models/appointmentModel');

const User = require('../models/userModel');
const asyncHandler = require('express-async-handler');

exports.getDoctorAppointments = asyncHandler(async (req, res) => {
  const { status, date } = req.query;
  const doctorId = req.user.id;

  const query = { doctorId };
  if (status) query.status = status;
  if (date) {
    const startDate = new Date(date);
    startDate.setHours(0, 0, 0, 0);
    const endDate = new Date(date);
    endDate.setHours(23, 59, 59, 999);
    query.date = { $gte: startDate, $lte: endDate };
  }

  const appointments = await Appointment.find(query)
    .populate('patientId', 'name email dateOfBirth gender bloodGroup')
    .sort({ date: 1, time: 1 });

  res.status(200).json({
    success: true,
    count: appointments.length,
    data: appointments
  });
});


exports.getPatientDetails = asyncHandler(async (req, res) => {
  const patientId = req.params.id;
  const doctorId = req.user.id;

  const hasAppointments = await Appointment.exists({
    patientId,
    doctorId,
    status: { $in: ['scheduled', 'completed'] }
  });

  if (!hasAppointments) {
    return res.status(403).json({
      success: false,
      message: 'Not authorized to access this patient'
    });
  }

  const [patient, medicalHistory, prescriptions] = await Promise.all([
    User.findById(patientId).select('-password'),
    MedicalHistory.findOne({ patientId }),
    Prescription.find({ patientId }).sort({ date: -1 })
  ]);

  res.status(200).json({
    success: true,
    data: {
      patient,
      medicalHistory: medicalHistory || {},
      prescriptions
    }
  });
});


exports.updateAppointmentStatus = asyncHandler(async (req, res) => {
  const { status } = req.body;
  const appointmentId = req.params.id;
  const doctorId = req.user.id;

  if (!['completed', 'cancelled', 'no-show'].includes(status)) {
    return res.status(400).json({
      success: false,
      message: 'Invalid status value'
    });
  }

  const appointment = await Appointment.findOneAndUpdate(
    { _id: appointmentId, doctorId },
    { status },
    { new: true }
  );

  if (!appointment) {
    return res.status(404).json({
      success: false,
      message: 'Appointment not found'
    });
  }

  res.status(200).json({
    success: true,
    data: appointment
  });
});

exports.getDoctorPatients = asyncHandler(async (req, res) => {
  const doctorId = req.user.id;

  const appointments = await Appointment.find({ doctorId })
    .populate('patientId', 'name email dateOfBirth gender bloodGroup')
    .sort({ date: 1 });

  const patients = appointments.map(appointment => ({
    ...appointment.patientId.toObject(),
    id: appointment.patientId._id
  }));

  res.status(200).json({
    success: true,
    count: patients.length,
    data: patients
  });
});

exports.getPatientMedicalRecords = asyncHandler(async (req, res) => {
  const { patientId } = req.params;
  const doctorId = req.user.id;

  const hasAppointments = await Appointment.exists({
    patientId,
    doctorId,
    status: { $in: ['scheduled', 'completed'] }
  });

  if (!hasAppointments) {
    return res.status(403).json({
      success: false,
      message: 'Not authorized to access this patient\'s records'
    });
  }

  const medicalRecords = await MedicalHistory.find({ patientId, doctorId })
    .populate('doctorId', 'name specialization')
    .populate('patientId', 'name dateOfBirth gender');

  

  res.status(200).json({
    success: true,
    data: medicalRecords
  });
});

exports.getMedicalRecordDetails = asyncHandler(async (req, res) => {
  const { recordId } = req.params;
  const doctorId = req.user.id;

  const medicalRecord = await MedicalHistory.findOne({
    _id: recordId,
    doctorId
  })
    .populate('doctorId', 'name specialization')
    .populate('patientId', 'name dateOfBirth gender');

  if (!medicalRecord) {
    return res.status(404).json({
      success: false,
      message: 'Medical record not found or not authorized'
    });
  }

  res.status(200).json({
    success: true,
    data: medicalRecord
  });
});

exports.createMedicalRecord = asyncHandler(async (req, res) => {
  const { patientId, treatment, notes,diagnosis } = req.body;
  const doctorId = req.user.id;
console.log('Creating medical record:', req.body);
  const medicalHistory = await MedicalHistory.create({
    patientId,
    doctorId,
    diagnosis,  
    treatment,
    notes,
    lastUpdated: new Date()
  });
  console.log('Created medical history:', medicalHistory);

  res.status(201).json({
    success: true,
    data: medicalHistory
  });
});


exports.updateMedicalRecord = asyncHandler(async (req, res) => {
  const { recordId } = req.params;
  const { diagnosis, treatment, notes } = req.body;

  console.log('Updating medical record:', recordId);

  const medicalHistory = await MedicalHistory.findById(recordId);

  if (!medicalHistory) {
    return res.status(404).json({
      success: false,
      message: 'Medical record not found'
    });
  }

  medicalHistory.diagonosis = diagnosis || medicalHistory.diagonosis;
  medicalHistory.treatment = treatment || medicalHistory.treatment;
  medicalHistory.notes = notes || medicalHistory.notes;
  medicalHistory.lastUpdated = new Date();

  await medicalHistory.save();

  res.status(200).json({
    success: true,
    data: medicalHistory
  });
});

exports.deleteMedicalRecord = asyncHandler(async (req, res) => {
  const { recordId } = req.params;
  const doctorId = req.user.id;

  const medicalHistory = await MedicalHistory.findOneAndDelete({
    _id: recordId,
    doctorId: doctorId
  });

  if (!medicalHistory) {
    return res.status(404).json({
      success: false,
      message: 'Medical record not found or not authorized'
    });
  }

  res.status(200).json({
    success: true,
    message: 'Medical record deleted successfully',
    data: {
      patientId: medicalHistory.patientId
    }
  });
});



// @desc    Get all prescriptions for a specific patient
// @route   GET /api/doctor/patients/:patientId/prescriptions
// @access  Private/Doctor
exports.getPatientPrescriptions = asyncHandler(async (req, res) => {
  
  const { patientId } = req.params;
  const doctorId = req.user.id;

  const hasAppointments = await Appointment.exists({
    patientId,
    doctorId,
    status: { $in: ['scheduled', 'completed'] }
  });

  if (!hasAppointments) {
    return res.status(403).json({
      success: false,
      message: 'Not authorized to access this patient\'s prescriptions'
    });
  }

  const prescriptions = await Prescription.find({ patientId, doctorId })
 

  res.status(200).json({
    success: true,
    data: prescriptions
  });
});

// @desc    Get specific prescription details
// @route   GET /api/doctor/prescriptions/:prescriptionId
// @access  Private/Doctor
exports.getPrescriptionDetails = asyncHandler(async (req, res) => {
  const { prescriptionId } = req.params;
  const doctorId = req.user.id;

  

  const prescription = await Prescription.findOne({
    _id: prescriptionId,
    doctorId
  })

  if (!prescription) {
    return res.status(404).json({
      success: false,
      message: 'Prescription not found or not authorized'
    });
  }

  res.status(200).json({
    success: true,
    data: prescription
  });
});

// @desc    Create a new prescription
// @route   POST /api/doctor/prescriptions
// @access  Private/Doctor
exports.createPrescription = asyncHandler(async (req, res) => {
  console.log('Creating prescription:', req.body);
  const { patientId,  medications } = req.body;
  const doctorId = req.user.id;

  const newPrescription = await Prescription.create({
    patientId,
    doctorId,
  
    medications
  });

  res.status(201).json({
    success: true,
    data: newPrescription
  });
});

// @desc    Update a prescription
// @route   PUT /api/doctor/prescriptions/:prescriptionId
// @access  Private/Doctor
exports.updatePrescription = asyncHandler(async (req, res) => {
  const { prescriptionId } = req.params;
  const { medications } = req.body;

  const prescription = await Prescription.findOne({
    _id: prescriptionId,
    doctorId: req.user.id
  });

  if (!prescription) {
    return res.status(404).json({
      success: false,
      message: 'Prescription not found or not authorized'
    });
  }

  prescription.medications = medications || prescription.medications;

  await prescription.save();

  res.status(200).json({
    success: true,
    data: prescription
  });
});

// @desc    Delete a prescription
// @route   DELETE /api/doctor/prescriptions/:prescriptionId
// @access  Private/Doctor
exports.deletePrescription = asyncHandler(async (req, res) => {
  const { prescriptionId } = req.params;

  const prescription = await Prescription.findOneAndDelete({
    _id: prescriptionId,
    doctorId: req.user.id
  });

  if (!prescription) {
    return res.status(404).json({
      success: false,
      message: 'Prescription not found or not authorized'
    });
  }

  res.status(200).json({
    success: true,
    message: 'Prescription deleted successfully',
    data: {
      patientId: prescription.patientId
    }
  });
});
