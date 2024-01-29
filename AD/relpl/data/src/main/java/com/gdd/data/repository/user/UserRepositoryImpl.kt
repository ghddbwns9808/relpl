package com.gdd.data.repository.user

import com.gdd.data.mapper.toHistoryList
import com.gdd.data.mapper.toPointRecord
import com.gdd.data.mapper.toSignUpResult
import com.gdd.data.mapper.toUser
import com.gdd.data.model.signin.SignInRequest
import com.gdd.data.model.signup.SignupRequest
import com.gdd.data.repository.user.remote.UserRemoteDataSource
import com.gdd.domain.model.history.History
import com.gdd.domain.model.point.PointRecord
import com.gdd.domain.model.user.SignUpResult
import com.gdd.domain.model.user.User
import com.gdd.domain.repository.UserRepository
import java.io.File
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource
) : UserRepository {
    override suspend fun signIn(userUid: String, userPasswork: String): Result<User> {
        return userRemoteDataSource.signIn(
            SignInRequest(userUid, userPasswork)
        ).map { it.toUser() }
    }

    override suspend fun isDuplicatedPhone(phone: String): Result<Boolean> {
        return userRemoteDataSource.isDuplicatedPhone(phone)
    }

    override suspend fun isDuplicatedId(id: String): Result<Boolean> {
        return userRemoteDataSource.isDuplicatedId(id)
    }

    override suspend fun isDuplicatedNickname(nickname: String): Result<Boolean> {
        return userRemoteDataSource.isDuplicatedNickname(nickname)
    }

    override suspend fun signUp(phone: String, id: String, pw: String, nickname: String): Result<SignUpResult>{
        return userRemoteDataSource.signUp(
            SignupRequest(
                id,pw,nickname,phone
            )
        ).map {
            it.toSignUpResult()
        }
    }

    override suspend fun registerProfileImage(img: File, userId: Long): Result<Boolean> {
        return userRemoteDataSource.registerProfileImage(img, userId)
    }

    override suspend fun changePassword(
        userId: Long,
        currentPassword: String,
        newPassword: String
    ): Result<Boolean> {
        return userRemoteDataSource.changePassword(userId, currentPassword, newPassword)
    }

    override suspend fun getCurrentPoint(userId: Long): Result<Int> {
        return userRemoteDataSource.getCurrentPoint(userId)
    }

    override suspend fun getPointRecord(userId: Long): Result<PointRecord> {
        return userRemoteDataSource.getPointRecord(userId)
            .map {
                it.toPointRecord()
            }
    }

    override suspend fun updateProfile(
        userProfilePhoto: File?,
        userId: Long,
        userNickname: String,
        userPhone: String
    ): Result<Boolean> {
        return userRemoteDataSource.updateProfile(
            userProfilePhoto,
            userId,
            userNickname,
            userPhone
        )
    }

    override suspend fun getHistory(userId: Long): Result<List<History>> {
        return userRemoteDataSource.getHistory(userId).map{
            it.toHistoryList()
        }
    }
}