#!/bin/bash

set -eu
set -o pipefail

cd "$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
cd ..

python3 paramgen.py --raw_parquet_dir ${LDBC_SNB_DATA_ROOT_DIRECTORY} --factor_tables_dir 'scratch/factors/' --time_bucket_size_in_days 1 --generate_short_query_parameters True
