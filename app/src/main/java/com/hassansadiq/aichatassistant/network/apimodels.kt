package com.HassanSadiq.AIChatAssistant.network

import com.google.gson.annotations.SerializedName

data class CreateUserRequest(
    @SerializedName("app_id")
    val appId: String,
    @SerializedName("table_name")
    val tableName: String,
    @SerializedName("data")
    val data: UserData
)

data class UserData(
    @SerializedName("provider")
    val provider: String
)

data class UserResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("provider")
    val provider: String,
    @SerializedName("created_at")
    val createdAt: String
)

data class AIResponse(
    @SerializedName("message")
    val message: AIMessage,
    @SerializedName("suggestions")
    val suggestions: List<String>?,
    @SerializedName("context")
    val context: AIContext?
)

data class AIMessage(
    @SerializedName("content")
    val content: String,
    @SerializedName("timestamp")
    val timestamp: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("metadata")
    val metadata: AIMetadata?
)

data class AIMetadata(
    @SerializedName("model")
    val model: String?,
    @SerializedName("confidence_score")
    val confidenceScore: Double?,
    @SerializedName("message_id")
    val messageId: String?
)

data class AIContext(
    @SerializedName("conversation_id")
    val conversationId: String?,
    @SerializedName("message_count")
    val messageCount: Int?,
    @SerializedName("last_interaction")
    val lastInteraction: String?
)