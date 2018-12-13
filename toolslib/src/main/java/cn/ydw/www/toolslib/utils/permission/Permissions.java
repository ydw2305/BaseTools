/**
 * Copyright 2015 XiaoMu
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package cn.ydw.www.toolslib.utils.permission;

/**
 * Enum class to handle the different states
 * of permissions since the PackageManager only
 * has a granted and denied state.
 * @see Enum  GRANTED  同意
 * @see Enum DENIED  拒绝
 * @see Enum NOT_FOUND  未找到
 */
enum Permissions {
    GRANTED,/**同意*/
    DENIED,/**拒绝*/
    NOT_FOUND/**未找到*/
}