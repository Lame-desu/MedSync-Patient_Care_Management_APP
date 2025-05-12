//package com.example.myapplication.data.repository.doctor
//
//import com.example.myapplication.data.model.AuthPreferences
//import com.example.myapplication.data.model.doctor.AppointmentResponse
//import com.example.myapplication.data.model.doctor.Doctor
//import com.example.myapplication.data.model.doctor.PatientDetailsResponse
//import com.example.myapplication.data.model.doctor.UpdateStatusRequest
//import com.example.myapplication.data.remote.DoctorApi
//import com.example.myapplication.data.remote.NetworkProvider
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.flow
//import kotlinx.coroutines.flow.flowOn
//import retrofit2.HttpException
//import java.io.IOException
//import java.text.SimpleDateFormat
//import java.util.*
//import android.util.Log
//
//class DoctorRepository(
//    private val authPreferences: AuthPreferences,
//    private val doctorApi: DoctorApi = NetworkProvider.doctorApi
//) {
//    fun getDoctors(): Flow<List<Doctor>> = flow {
//        try {
//            val token = authPreferences.getToken() ?: throw IllegalStateException("No token found")
//            val response = doctorApi.getDoctors("Bearer $token")
//            if (response.success) {
//                emit(response.data.map { it.copy(id = it.id ?: generateFallbackId()) })
//            } else {
//                throw Exception("API error: ${response.message}")
//            }
//        } catch (e: HttpException) {
//            throw Exception("Network error: ${e.message()}")
//        } catch (e: IOException) {
//            throw Exception("IO error: ${e.message}")
//        }
//    }.flowOn(Dispatchers.IO)
//
//    fun getDoctorAppointments(date: String? = null, status: String? = null): Flow<AppointmentResponse> = flow {
//        val token = authPreferences.getToken() ?: throw Exception("No token")
//        try {
//            Log.d("DoctorRepository", "Fetching appointments with token: Bearer $token, date: $date, status: $status")
//            val response = doctorApi.getDoctorAppointments("Bearer $token", status, date)
//            Log.d("DoctorRepository", "Appointments response: $response")
//            if (response.success) {
//                emit(response)
//            } else {
//                throw Exception(response.message ?: "Failed to fetch appointments")
//            }
//        } catch (e: Exception) {
//            Log.e("DoctorRepository", "Error fetching appointments: ${e.message}", e)
//            throw Exception("Failed to fetch appointments: ${e.message}")
//        }
//    }.flowOn(Dispatchers.IO)
//
//    fun getPatientDetails(patientId: String): Flow<PatientDetailsResponse> = flow {
//        val token = authPreferences.getToken() ?: throw Exception("No token")
//        try {
//            Log.d("DoctorRepository", "Fetching patient details for $patientId with token: Bearer $token")
//            val response = doctorApi.getPatientDetails("Bearer $token", patientId)
//            Log.d("DoctorRepository", "Patient details response: $response")
//            if (response.success) {
//                emit(response)
//            } else {
//                throw Exception(response.message ?: "Failed to fetch patient details")
//            }
//        } catch (e: Exception) {
//            Log.e("DoctorRepository", "Error fetching patient details: ${e.message}", e)
//            throw Exception("Failed to fetch patient details: ${e.message}")
//        }
//    }.flowOn(Dispatchers.IO)
//
//    fun updateAppointmentStatus(appointmentId: String, status: String): Flow<AppointmentResponse> = flow {
//        val token = authPreferences.getToken() ?: throw Exception("No token")
//        try {
//            Log.d("DoctorRepository", "Updating appointment $appointmentId to status $status with token: Bearer $token")
//            val request = UpdateStatusRequest(status)
//            val response = doctorApi.updateAppointmentStatus("Bearer $token", appointmentId, request)
//            Log.d("DoctorRepository", "Update status response: $response")
//            if (response.success) {
//                emit(response)
//            } else {
//                throw Exception(response.message ?: "Failed to update appointment status")
//            }
//        } catch (e: Exception) {
//            Log.e("DoctorRepository", "Error updating appointment status: ${e.message}", e)
//            throw Exception("Failed to update appointment status: ${e.message}")
//        }
//    }.flowOn(Dispatchers.IO)
//
//    fun formatDateForApi(date: Date): String {
//        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//        return sdf.format(date)
//    }
//
//    private fun generateFallbackId(): String {
//        return "fallback_${System.currentTimeMillis()}"
//    }
//}
//
//package com.example.myapplication.data.repository.doctor
//
//import com.example.myapplication.data.model.AuthPreferences
//import com.example.myapplication.data.model.doctor.AppointmentResponse
//import com.example.myapplication.data.model.doctor.Doctor
//import com.example.myapplication.data.model.doctor.PatientDetailsResponse
//import com.example.myapplication.data.model.doctor.UpdateStatusRequest
//import com.example.myapplication.data.remote.DoctorApi
//import com.example.myapplication.data.remote.NetworkProvider
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.flow
//import kotlinx.coroutines.flow.flowOn
//import retrofit2.HttpException
//import java.io.IOException
//import java.text.SimpleDateFormat
//import java.util.*
//import android.util.Log
//import com.example.myapplication.data.model.patient.PatientResponse
//
//class DoctorRepository(
//    private val authPreferences: AuthPreferences,
//    private val doctorApi: DoctorApi = NetworkProvider.doctorApi
//) {
//    fun getDoctors(): Flow<List<Doctor>> = flow {
//        try {
//            val token = authPreferences.getToken() ?: throw IllegalStateException("No token found")
//            val response = doctorApi.getDoctors("Bearer $token")
//            if (response.success) {
//                emit(response.data.map { it.copy(id = it.id ?: generateFallbackId()) })
//            } else {
//                throw Exception("API error: ${response.message}")
//            }
//        } catch (e: HttpException) {
//            throw Exception("Network error: ${e.message()}")
//        } catch (e: IOException) {
//            throw Exception("IO error: ${e.message}")
//        }
//    }.flowOn(Dispatchers.IO)
//
//    fun getDoctorAppointments(date: String?, status: String?): Flow<AppointmentResponse> = flow {
//        try {
//            val token = authPreferences.getToken() ?: throw Exception("No token found")
//            val response = doctorApi.getDoctorAppointments("Bearer $token", date, status)
//            if (response.success) {
//                Log.d("DoctorRepository", "Successfully fetched appointments: ${response.data}")
//                emit(response)
//            } else {
//                throw Exception("API call unsuccessful")
//            }
//        } catch (e: Exception) {
//            Log.e("DoctorRepository", "Error fetching appointments: ${e.message}", e)
//            throw e
//        }
//    }
//
//    fun getDoctorPatients(): Flow<PatientResponse> = flow {
//        try {
//            val token = authPreferences.getToken() ?: throw Exception("No token found")
//            val response = doctorApi.getDoctorPatients("Bearer $token")
//            if (response.success) {
//                Log.d("DoctorRepository", "Successfully fetched patients: ${response.data}")
//                emit(response)
//            } else {
//                throw Exception("API call unsuccessful")
//            }
//        } catch (e: Exception) {
//            Log.e("DoctorRepository", "Error fetching patients: ${e.message ?: "Unknown error"}", e)
//            throw e
//        }
//    }
//
//    fun getPatientDetails(patientId: String): Flow<PatientDetailsResponse> = flow {
//        try {
//            val token = authPreferences.getToken() ?: throw Exception("No token found")
//            val response = doctorApi.getPatientDetails("Bearer $token", patientId)
//            if (response.success) {
//                Log.d("DoctorRepository", "Successfully fetched patient details: ${response.data}")
//                emit(response)
//            } else {
//                throw Exception("API call unsuccessful")
//            }
//        } catch (e: Exception) {
//            Log.e("DoctorRepository", "Error fetching patient details: ${e.message ?: "Unknown error"}", e)
//            throw e
//        }
//    }
//
//
////    fun updateAppointmentStatus(appointmentId: String, status: String): Flow<AppointmentResponse> = flow {
////        val token = authPreferences.getToken() ?: throw Exception("No token")
////        try {
////            Log.d("DoctorRepository", "Updating appointment $appointmentId to status $status with token: Bearer $token")
////            val request = UpdateStatusRequest(status)
////            val response = doctorApi.updateAppointmentStatus("Bearer $token", appointmentId, request)
////            Log.d("DoctorRepository", "Update status response: $response")
////            if (response.success) {
////                emit(response)
////            } else {
////                throw Exception(response.message?: "Failed to update appointment status")
////            }
////        } catch (e: Exception) {
////            Log.e("DoctorRepository", "Error updating appointment status: ${e.message}", e)
////            throw Exception("Failed to update appointment status: ${e.message}")
////        }
////    }.flowOn(Dispatchers.IO)
//
//    fun formatDateForApi(date: Date): String {
//        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//        return sdf.format(date)
//    }
//
//    private fun generateFallbackId(): String {
//        return "fallback_${System.currentTimeMillis()}"
//    }
//}
//


















package com.example.myapplication.data.repository.doctor

import android.util.Log
import com.example.myapplication.data.model.AuthPreferences
import com.example.myapplication.data.model.doctor.*
import com.example.myapplication.data.model.patient.PatientResponse
import com.example.myapplication.data.remote.DoctorApi
import com.example.myapplication.data.remote.NetworkProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class DoctorRepository(
    private val authPreferences: AuthPreferences,
    private val doctorApi: DoctorApi = NetworkProvider.doctorApi
) {
    fun getDoctors(): Flow<List<Doctor>> = flow {
        try {
            val token = authPreferences.getToken() ?: throw IllegalStateException("No token found")
            val response = doctorApi.getDoctors("Bearer $token")
            if (response.success) {
                emit(response.data.map { it.copy(id = it.id ?: generateFallbackId()) })
            } else {
                throw Exception("API error: ${response.message}")
            }
        } catch (e: HttpException) {
            throw Exception("Network error: ${e.message()}")
        } catch (e: IOException) {
            throw Exception("IO error: ${e.message}")
        }
    }.flowOn(Dispatchers.IO)

    fun getDoctorAppointments(date: String?, status: String?): Flow<AppointmentResponse> = flow {
        try {
            val token = authPreferences.getToken() ?: throw Exception("No token found")
            val response = doctorApi.getDoctorAppointments("Bearer $token", date, status)
            if (response.success) {
                Log.d("DoctorRepository", "Successfully fetched appointments: ${response.data}")
                emit(response)
            } else {
                throw Exception("API call unsuccessful")
            }
        } catch (e: Exception) {
            Log.e("DoctorRepository", "Error fetching appointments: ${e.message}", e)
            throw e
        }
    }.flowOn(Dispatchers.IO)

    fun getDoctorPatients(): Flow<PatientResponse> = flow {
        try {
            val token = authPreferences.getToken() ?: throw Exception("No token found")
            val response = doctorApi.getDoctorPatients("Bearer $token")
            if (response.success) {
                Log.d("DoctorRepository", "Successfully fetched patients: ${response.data}")
                emit(response)
            } else {
                throw Exception("API call unsuccessful")
            }
        } catch (e: Exception) {
            Log.e("DoctorRepository", "Error fetching patients: ${e.message ?: "Unknown error"}", e)
            throw e
        }
    }.flowOn(Dispatchers.IO)

    fun getPatientDetails(patientId: String): Flow<PatientDetailsResponse> = flow {
        try {
            val token = authPreferences.getToken() ?: throw Exception("No token found")
            val response = doctorApi.getPatientDetails("Bearer $token", patientId)
            if (response.success) {
                Log.d("DoctorRepository", "Successfully fetched patient details: ${response.data}")
                emit(response)
            } else {
                throw Exception("API call unsuccessful")
            }
        } catch (e: Exception) {
            Log.e("DoctorRepository", "Error fetching patient details: ${e.message ?: "Unknown error"}", e)
            throw e
        }
    }.flowOn(Dispatchers.IO)

    fun getPatientMedicalRecords(patientId: String): Flow<MedicalRecordsResponse> = flow {
        try {
            val token = authPreferences.getToken() ?: throw Exception("No token found")
            val response = doctorApi.getPatientMedicalRecords("Bearer $token", patientId)
            if (response.success) {
                Log.d("DoctorRepository", "Successfully fetched medical records: ${response.data}")
                emit(response)
            } else {
                throw Exception("API call unsuccessful: ${response.message}")
            }
        } catch (e: Exception) {
            Log.e("DoctorRepository", "Error fetching medical records: ${e.message ?: "Unknown error"}", e)
            throw e
        }
    }.flowOn(Dispatchers.IO)

    fun getMedicalRecordDetails(recordId: String): Flow<MedicalRecordResponse> = flow {
        try {
            val token = authPreferences.getToken() ?: throw Exception("No token found")
            val response = doctorApi.getMedicalRecordDetails("Bearer $token", recordId)
            if (response.success) {
                Log.d("DoctorRepository", "Successfully fetched medical record details: ${response.data}")
                emit(response)
            } else {
                throw Exception("API call unsuccessful: ${response.message}")
            }
        } catch (e: Exception) {
            Log.e("DoctorRepository", "Error fetching medical record details: ${e.message ?: "Unknown error"}", e)
            throw e
        }
    }.flowOn(Dispatchers.IO)

    fun createMedicalRecord(request: CreateMedicalRecordRequest): Flow<MedicalRecordResponse> = flow {
        try {
            val token = authPreferences.getToken() ?: throw Exception("No token found")
            val response = doctorApi.createMedicalRecord("Bearer $token", request)
            if (response.success) {
                Log.d("DoctorRepository", "Successfully created medical record: ${response.data}")
                emit(response)
            } else {
                throw Exception("API call unsuccessful: ${response.message}")
            }
        } catch (e: Exception) {
            Log.e("DoctorRepository", "Error creating medical record: ${e.message ?: "Unknown error"}", e)
            throw e
        }
    }.flowOn(Dispatchers.IO)

    fun updateMedicalRecord(recordId: String, request: UpdateMedicalRecordRequest): Flow<MedicalRecordResponse> = flow {
        try {
            val token = authPreferences.getToken() ?: throw Exception("No token found")
            val response = doctorApi.updateMedicalRecord("Bearer $token", recordId, request)
            if (response.success) {
                Log.d("DoctorRepository", "Successfully updated medical record: ${response.data}")
                emit(response)
            } else {
                throw Exception("API call unsuccessful: ${response.message}")
            }
        } catch (e: Exception) {
            Log.e("DoctorRepository", "Error updating medical record: ${e.message ?: "Unknown error"}", e)
            throw e
        }
    }.flowOn(Dispatchers.IO)

    fun deleteMedicalRecord(recordId: String): Flow<DeleteMedicalRecordResponse> = flow {
        try {
            val token = authPreferences.getToken() ?: throw Exception("No token found")
            val response = doctorApi.deleteMedicalRecord("Bearer $token", recordId)
            if (response.success) {
                Log.d("DoctorRepository", "Successfully deleted medical record")
                emit(response)
            } else {
                throw Exception("API call unsuccessful: ${response.message}")
            }
        } catch (e: Exception) {
            Log.e("DoctorRepository", "Error deleting medical record: ${e.message ?: "Unknown error"}", e)
            throw e
        }
    }.flowOn(Dispatchers.IO)

    fun formatDateForApi(date: Date): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(date)
    }

    private fun generateFallbackId(): String {
        return "fallback_${System.currentTimeMillis()}"
    }
    fun getPatientPrescriptions(patientId: String): Flow<PrescriptionListResponse> = flow {
        try {
            val token = authPreferences.getToken() ?: throw Exception("No token found")
            val response = doctorApi.getPatientPrescriptions("Bearer $token", patientId)
            if (response.success) {
                Log.d("DoctorRepository", "Successfully fetched prescriptions: ${response.prescriptions}")
                emit(response)
            } else {
                throw Exception("API call unsuccessful: ${response.message}")
            }
        } catch (e: Exception) {
            Log.e("DoctorRepository", "Error fetching prescriptions: ${e.message}", e)
            throw e
        }
    }.flowOn(Dispatchers.IO)

    fun getPrescriptionDetails(prescriptionId: String): Flow<PrescriptionDetailResponse> = flow {
        try {
            val token = authPreferences.getToken() ?: throw Exception("No token found")
            val response = doctorApi.getPrescriptionDetails("Bearer $token", prescriptionId)
            if (response.success) {
                Log.d("DoctorRepository", "Successfully fetched prescription details: ${response.prescription}")
                emit(response)
            } else {
                throw Exception("API call unsuccessful: ${response.message}")
            }
        } catch (e: Exception) {
            Log.e("DoctorRepository", "Error fetching prescription details: ${e.message}", e)
            throw e
        }
    }.flowOn(Dispatchers.IO)

    fun createPrescription(request: CreatePrescriptionRequest): Flow<PrescriptionDetailResponse> = flow {
        try {
            val token = authPreferences.getToken() ?: throw Exception("No token found")
            val response = doctorApi.createPrescription("Bearer $token", request)
            if (response.success) {
                Log.d("DoctorRepository", "Successfully created prescription: ${response.prescription}")
                emit(response)
            } else {
                throw Exception("API call unsuccessful: ${response.message}")
            }
        } catch (e: Exception) {
            Log.e("DoctorRepository", "Error creating prescription: ${e.message}", e)
            throw e
        }
    }.flowOn(Dispatchers.IO)

    fun updatePrescription(prescriptionId: String, request: UpdatePrescriptionRequest): Flow<PrescriptionDetailResponse> = flow {
        try {
            val token = authPreferences.getToken() ?: throw Exception("No token found")
            val response = doctorApi.updatePrescription("Bearer $token", prescriptionId, request)
            if (response.success) {
                Log.d("DoctorRepository", "Successfully updated prescription: ${response.prescription}")
                emit(response)
            } else {
                throw Exception("API call unsuccessful: ${response.message}")
            }
        } catch (e: Exception) {
            Log.e("DoctorRepository", "Error updating prescription: ${e.message}", e)
            throw e
        }
    }.flowOn(Dispatchers.IO)

    fun deletePrescription(prescriptionId: String): Flow<DeletePrescriptionResponse> = flow {
        try {
            val token = authPreferences.getToken() ?: throw Exception("No token found")
            val response = doctorApi.deletePrescription("Bearer $token", prescriptionId)
            if (response.success) {
                Log.d("DoctorRepository", "Successfully deleted prescription")
                emit(response)
            } else {
                throw Exception("API call unsuccessful: ${response.message}")
            }
        } catch (e: Exception) {
            Log.e("DoctorRepository", "Error deleting prescription: ${e.message}", e)
            throw e
        }
    }
}