package com.edunova.di

import android.content.Context
import com.edunova.ai.*

object AIModule {
    
    @Volatile
    private var aiLearningEngine: OfflineAILearningEngine? = null
    @Volatile
    private var voiceTutor: VoiceTutor? = null
    @Volatile
    private var languageTranslator: LanguageTranslator? = null
    @Volatile
    private var adaptiveLearningEngine: AdaptiveLearningEngine? = null
    @Volatile
    private var gamificationEngine: GamificationEngine? = null
    
    fun getOfflineAILearningEngine(context: Context): OfflineAILearningEngine {
        return aiLearningEngine ?: synchronized(this) {
            aiLearningEngine ?: OfflineAILearningEngine(context).also { aiLearningEngine = it }
        }
    }
    
    fun getVoiceTutor(context: Context): VoiceTutor {
        return voiceTutor ?: synchronized(this) {
            voiceTutor ?: VoiceTutor(context).also { voiceTutor = it }
        }
    }
    
    fun getLanguageTranslator(context: Context): LanguageTranslator {
        return languageTranslator ?: synchronized(this) {
            languageTranslator ?: LanguageTranslator(context).also { languageTranslator = it }
        }
    }
    
    fun getAdaptiveLearningEngine(): AdaptiveLearningEngine {
        return adaptiveLearningEngine ?: synchronized(this) {
            adaptiveLearningEngine ?: AdaptiveLearningEngine().also { adaptiveLearningEngine = it }
        }
    }
    
    fun getGamificationEngine(): GamificationEngine {
        return gamificationEngine ?: synchronized(this) {
            gamificationEngine ?: GamificationEngine().also { gamificationEngine = it }
        }
    }
}

