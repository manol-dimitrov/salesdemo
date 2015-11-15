module.exports = function(grunt) {
    'use strict';
    // Project configuration.
    grunt.initConfig({

        clean:{
            buildDir  : ["build/"]
        },

        concat: {
            dev: {
                src: ["app/app.js","app/src/controllers/*.js", "app/src/services/*.js", "app/src/config/dev/*.js"],
                dest: "build/dev/js/salesdemo-dev.js"
            },

            prod: {
                src: ["app/app.js","app/src/controllers/*.js", "app/src/services/*.js", "app/src/config/prod/*.js"],
                dest: "build/prod/js/salesdemo.js"
            }
        },

        copy:{

            devhtml:{
                src: 'app/html/index-dev.html',
                dest:'build/dev/index.html'
            },
            prodhtml:{
                src: 'app/html/index.html',
                dest:'build/prod/index.jsp'
            },
            orderdevhtml:{
                src: 'app/html/views/orders.html',
                dest:'build/dev/views/orders.html'
            },
            orderprodhtml:{
                src: 'app/html/views/orders.html',
                dest:'build/prod/orders.jsp'
            },

            productsdevhtml:{
                src: 'app/html/views/products.html',
                dest:'build/dev/views/products.html'
            },
            productsprodhtml:{
                src: 'app/html/views/products.html',
                dest:'build/prod/products.jsp'
            },

            updateorderdevhtml:{
                src: 'app/html/views/updateorder.html',
                dest:'build/dev/views/updateorder.html'
            },
            updateorderprodhtml:{
                src: 'app/html/views/updateorder.html',
                dest:'build/prod/updateorder.jsp'
            },

            devcss:{
                files: [
                    {expand:true, cwd: 'app/',flatten: false,src:['css/**'], dest:'build/dev/'}
                ]
            },
            prodcss:{
                files: [
                    {expand:true, cwd: 'app/',flatten: false,src:['css/**'], dest:'build/prod/'}
                ]
            }
        }


    });
    grunt.loadNpmTasks('grunt-contrib-clean');
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-copy');
    grunt.loadNpmTasks('grunt-git-release');
    grunt.loadNpmTasks('grunt-version');

    grunt.registerTask( 'build', ['clean', 'concat', 'copy']);
};