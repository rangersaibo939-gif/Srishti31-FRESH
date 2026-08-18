package com.srishti.ai.tool

import android.content.Context
import android.os.Build
import com.srishti.ai.tool.risk.ToolRisk

class AppInfoTool(
    context: Context,
    private val packageIdentifier: String
) : Tool {
    private val appContext = context.applicationContext

    override val name: String = "app_info"
    override val description: String = "Reads basic information about an installed application."
    override val risk: ToolRisk = ToolRisk.SAFE

    override fun execute(): ToolResult {
        val packageName = packageIdentifier.trim()
        if (!PACKAGE_PATTERN.matches(packageName)) {
            return ToolResult.failure(
                message = "A valid application package identifier is required.",
                error = "INVALID_PACKAGE_IDENTIFIER"
            )
        }

        return try {
            val packageManager = appContext.packageManager
            val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.longVersionCode
            } else {
                packageInfo.versionCode.toLong()
            }
            ToolResult.success(
                "Package: $packageName\n" +
                    "Name: ${applicationInfo.loadLabel(packageManager)}\n" +
                    "Version: ${packageInfo.versionName ?: "unknown"} ($versionCode)"
            )
        } catch (exception: Exception) {
            ToolResult.failure("Unable to read installed-app information.", exception.message)
        }
    }

    private companion object {
        val PACKAGE_PATTERN = Regex("^[A-Za-z][A-Za-z0-9_]*(\\.[A-Za-z][A-Za-z0-9_]*)+$")
    }
}
