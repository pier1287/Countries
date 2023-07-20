module.exports = async ({github, context, files}) => {
    await github.rest.issues.createComment({
        issue_number: context.issue.number,
        owner: context.repo.owner,
        repo: context.repo.repo,
        body: '⚠️ Ciao ho trovato ' + files.length + ' files .xml'
    })
}