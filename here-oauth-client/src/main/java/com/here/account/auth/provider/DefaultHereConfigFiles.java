/*
 * Copyright (c) 2018 HERE Europe B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.here.account.auth.provider;

import java.io.File;

/**
 * @author kmccrack
 */
class DefaultHereConfigFiles {
    private static final String USER_DOT_HOME = "user.home";
    private static final String DOT_HERE_SUBDIR = ".here";

    static File getDefaultHereConfigFile(String name) {
        String userDotHome = System.getProperty(USER_DOT_HOME);
        if (userDotHome != null && userDotHome.length() > 0) {
            File dir = new File(userDotHome, DOT_HERE_SUBDIR);
            File file = new File(dir, name);
            return file;
        }
        return null;
    }

    static File getDefaultHereConfigFile(String name, String path) {
        File dir = new File(path);
    	return new File( dir, name );
    }
}
