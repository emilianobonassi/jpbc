# CMake module for finding the PBC includes and libraries

# PATHS seems to be optional in the following two commands,
# despite what the man page says

find_path(INCLUDE_PATH_PBC pbc.h PATH_SUFFIXES pbc)
find_library(LIB_PATH_PBC NAMES pbc)
if (INCLUDE_PATH_PBC AND LIB_PATH_PBC)
   set(PBC_FOUND TRUE)
endif (INCLUDE_PATH_PBC AND LIB_PATH_PBC)

if (PBC_FOUND)
   if (NOT PBC_FIND_QUIETLY)
      message(STATUS "Found PBC: ${LIB_PATH_PBC}")
   endif (NOT PBC_FIND_QUIETLY)
else (PBC_FOUND)
   if (PBC_FIND_REQUIRED)
      message(FATAL_ERROR "Could not find PBC library")
   endif (PBC_FIND_REQUIRED)
endif (PBC_FOUND)
