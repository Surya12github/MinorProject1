  // Function to switch visible section
  function showSection(sectionId) {
    const sections = document.querySelectorAll('section');
    sections.forEach(sec => sec.classList.remove('active'));
    document.getElementById(sectionId).classList.add('active');

    // Smooth scroll to the selected section
    document.getElementById(sectionId).scrollIntoView({ behavior: 'smooth' });
  }

  // Navbar hide/show on scroll
  let prevScrollPos = window.pageYOffset;
  const navbar = document.getElementById("navbar");

  window.onscroll = function() {
    let currentScrollPos = window.pageYOffset;
    if (prevScrollPos > currentScrollPos) {
      navbar.style.top = "0"; // show navbar
    } else {
      navbar.style.top = "-70px"; // hide navbar
    }
    prevScrollPos = currentScrollPos;
  }

  // Get Started button — show careers section and scroll to it
  function scrollToTop() {
    showSection('careers');
    document.getElementById('careers').scrollIntoView({ behavior: 'smooth' });
    navbar.style.top = "0"; // make sure navbar is visible
  }
 