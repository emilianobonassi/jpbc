#==========================
#
# JPBC-PBC build system
#
# Main input file for CMake
#
#==========================

#================
# Configure CMake
#================

cmake_minimum_required(VERSION 2.4)

if(COMMAND cmake_policy)
  cmake_policy(SET CMP0003 NEW)
endif(COMMAND cmake_policy)

#=======================
# Set the project's name
#=======================

	project( JPBC-PBC )
    set(CMAKE_MODULE_PATH .)

#========
# Options
#========

#===============================================
# Set the default paths for binary and lib build
#===============================================

	get_filename_component( LIBRARY_OUTPUT_PATH ${CMAKE_BINARY_DIR}/build ABSOLUTE CACHE )

#========================================
# Set the default build type to "Release"
#========================================

	IF(NOT CMAKE_BUILD_TYPE)
		SET(CMAKE_BUILD_TYPE Release CACHE STRING "Choose the type of build, options are: None Debug Release RelWithDebInfo MinSizeRel." FORCE)
	ENDIF(NOT CMAKE_BUILD_TYPE)


#====================================================
# Collect all source files recursively to be compiled
#====================================================

	file( GLOB_RECURSE jpbc-pbc-source src/main/c/*.h )
	file( GLOB_RECURSE jpbc-pbc-source src/main/c/*.c )

#============================================================
# Add include and library directories for the compiler/linker
#============================================================

    find_package(GMP REQUIRED)
    find_package(PBC REQUIRED)

    set ( LIB_PATH_GMP   CACHE PATH "GMP lib path" )
    set ( INCLUDE_PATH_GMP   CACHE PATH "GMP include path" )
    link_directories( ${LIB_PATH_GMP} )
    include_directories ( ${INCLUDE_PATH_GMP} )

    set ( LIB_PATH_PBC   CACHE PATH "PBC lib path" )
    set ( INCLUDE_PATH_PBC   CACHE PATH "PBC include path" )
    link_directories( ${LIB_PATH_PBC} )
    include_directories ( ${INCLUDE_PATH_PBC} )


#=============
# Build target
#=============

    add_library( jpbc-pbc SHARED ${jpbc-pbc-source})
    target_link_libraries(jpbc-pbc gmp pbc)