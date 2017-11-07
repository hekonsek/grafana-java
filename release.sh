# Licensed to the Spring Boot Docker Spotify project under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

#!/usr/bin/env bash

set -e

OLD_VERSION="${1}"
NEW_VERSION="${2}"

if [ -z "${OLD_VERSION}" ] || [ -z "${NEW_VERSION}" ]; then
    echo 'Please specify the latest and the new version of Spring Boot Docker Spotify. For example:'
    echo
    echo '  release.sh 0.0.5 0.0.6'
    exit
fi

mvn -Darguments="-Dmaven.test.skip=true -Dgpg.passphrase=${GPG_PASSWORD}" release:prepare release:perform

sed -i -e "s/${OLD_VERSION}/${NEW_VERSION}/g" readme.md