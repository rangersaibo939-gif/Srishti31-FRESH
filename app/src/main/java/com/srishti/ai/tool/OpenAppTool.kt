package com.srishti.ai.tool

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.srishti.ai.tool.risk.ToolRisk

class OpenAppTool(
    context: Context,
    private val packageIdentifier: String
) : Tool {
    private val appContext = context.applicationContext

    override val name: String = "open_app"
    override val description: String = "Launches an installed Android application."
    override val risk: ToolRisk = ToolRisk.SAFE

    override fun execute(): ToolResult {
        val packageName = packageIdentifier.trim()
        if (packageName.isEmpty()) {
            return ToolResult.failure(
                message = "Application package identifier is required.",
                error = "EMPTY_PACKAGE_IDENTIFIER"
            )
        }

        return try {
            val packageManager = appContext.packageManager
            packageManager.getApplicationInfo(packageName, 0)
            val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
                ?: return ToolResult.failure(
                    message = "No launchable activity was found for $packageName.",
                    error = "NO_LAUNCHABLE_ACTIVITY"
                )

            if (launchIntent.resolveActivity(packageManager) == null) {
                return ToolResult.failure(
                    message = "The launch activity for $packageName cannot be resolved.",
                    error = "UNRESOLVED_LAUNCH_ACTIVITY"
                )
            }

            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            appContext.startActivity(launchIntent)
            ToolResult.success("Launch request submitted for $packageName.")
        } catch (exception: PackageManager.NameNotFoundException) {
            ToolResult.failure(
                message = "Application $packageName is not installed.",
                error = "PACKAGE_NOT_FOUND"
            )
        } catch (exception: Exception) {
            ToolResult.failure(
                message = "Unable to launch $packageName.",
                error = exception.message
            )
        }
    }
}
